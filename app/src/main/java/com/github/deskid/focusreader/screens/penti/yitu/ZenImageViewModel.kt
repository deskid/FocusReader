package com.github.deskid.focusreader.screens.penti.yitu

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.api.data.NetworkState
import com.github.deskid.focusreader.api.data.ZenImage
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.ArticleEntity
import com.github.deskid.focusreader.db.entity.YituEntity
import com.github.logutils.LogUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import javax.inject.Inject

class ZenImageViewModel @Inject
constructor(private val appService: IAppService,
            private val appDatabase: AppDatabase) : ViewModel() {

    val zenImages: MutableLiveData<List<ZenImage>> = MutableLiveData()

    val zenImage: MutableLiveData<YituEntity> = MutableLiveData()

    val refreshState: MutableLiveData<NetworkState> = MutableLiveData()

    private val disposable: CompositeDisposable = CompositeDisposable()

    fun load(page: Int = 1) {
        disposable.add(appService.getZenImage(page)
                .map { response ->
                    when {
                        response.error > 0 -> emptyList()
                        else -> {
                            response.data?.forEach { zenImage -> zenImage.url = zenImage.description }
                            response.data
                        }
                    }
                }
                .doOnNext { appDatabase.articleDao().insertAll(articlesEntityWrap(it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = NetworkState.LOADING })
                .subscribe({
                    zenImages.value = it
                    refreshState.value = NetworkState.LOADED
                }, { refreshState.value = NetworkState.error(it.message) }))
    }

    fun loadZenImageDetail(url: String) {
        disposable.add(appDatabase.yituDao().findContentByUrl(url)
                .flatMap {
                    when {
                        it.isEmpty() -> appService.getContent(url).map {
                            val content = clean(it.string())
                            val yituEntity = YituEntity(0, content, url)
                            PentiResource.success(yituEntity)
                        }
                        else -> Flowable.just(PentiResource.success(it[0]))
                    }
                }
                .doOnNext {
                    LogUtils.d(Thread.currentThread().name)
                    appDatabase.yituDao().insert(it.data)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { zenImage.value = it.data })
    }

    private fun clean(string: String?): String {
        var result = string ?: ""
        result = result.replace(Regex("<title>.+?</title>"), "")
        result = Jsoup.clean(result, Whitelist.none())
        result = Jsoup.parse(result).text()
        return result
    }

    class ZenImageFactory @Inject
    constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ZenImageViewModel(appService, appDatabase) as T
        }
    }

    private fun articlesEntityWrap(zenImages: List<ZenImage>?): List<ArticleEntity> {
        return zenImages?.map { ArticleEntity.zenImageWrap(it) } ?: emptyList()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}