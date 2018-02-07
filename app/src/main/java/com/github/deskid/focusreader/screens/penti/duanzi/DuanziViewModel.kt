package com.github.deskid.focusreader.screens.penti.duanzi

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.data.Duanzi
import com.github.deskid.focusreader.api.data.NetworkState
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.ArticleEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import javax.inject.Inject

class DuanziViewModel @Inject
constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModel() {

    val refreshState: MutableLiveData<NetworkState> = MutableLiveData()

    val duanziList: MutableLiveData<List<Duanzi>> = MutableLiveData()

    private val disposable: CompositeDisposable = CompositeDisposable()

    fun load(page: Int = 1) {
        disposable.add(appService.getJoke(page)
                .map { response ->
                    when {
                        response.error > 0 -> {
                            refreshState.value = NetworkState.error(response.msg)
                            emptyList()
                        }
                        else -> {
                            response.data?.forEach { duanzi -> duanzi.description = clean(duanzi.description) }
                            response.data
                        }
                    }
                }
                .doOnNext { appDatabase.articleDao().insertAll(articlesEntityWrap(it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = NetworkState.LOADING })
                .subscribe({
                    duanziList.value = it
                    refreshState.value = NetworkState.LOADED
                }, { refreshState.value = NetworkState.error(it.message) }))
    }

    class JokeFactory @Inject
    constructor(private val mAppService: IAppService, private val mAppDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DuanziViewModel(mAppService, mAppDatabase) as T
        }
    }

    private fun articlesEntityWrap(duanzis: List<Duanzi>?): List<ArticleEntity> {
        return duanzis?.map { ArticleEntity.duanziWrap(it) } ?: emptyList()
    }

    private fun duanziWrap(articles: List<ArticleEntity>?): List<Duanzi> {
        return articles?.map { Duanzi(it) } ?: emptyList()
    }

    private fun clean(string: String?): String {
        var result = string ?: ""
        result = Jsoup.clean(result, Whitelist.none())
        result = Jsoup.parse(result).text()
        return result
    }

}