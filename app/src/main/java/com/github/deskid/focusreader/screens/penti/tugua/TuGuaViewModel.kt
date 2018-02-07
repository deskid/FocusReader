package com.github.deskid.focusreader.screens.penti.tugua

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.api.data.NetworkState
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.ArticleEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TuGuaViewModel @Inject
constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModel() {

    val refreshState: MutableLiveData<NetworkState> = MutableLiveData()

    val tuguas: MutableLiveData<List<TuGua>> = MutableLiveData()

    private val disposable: CompositeDisposable = CompositeDisposable()

    fun load(page: Int = 1) {
        val dbSource = Transformations.map(appDatabase.articleDao().findArticleByType(2, page)) {
            PentiResource.success(tuguaWrap(it))
        }

        disposable.add(appService.getTuGua(page)
                .map { response ->
                    when {
                        response.error > 0 -> {
                            refreshState.value = NetworkState.error(response.msg)
                            emptyList()
                        }
                        else -> {
                            response.data?.filter { !it.title.contentEquals("AD") }
                        }
                    }
                }.doOnNext { appDatabase.articleDao().insertAll(articlesEntityWrap(it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = NetworkState.LOADING })
                .subscribe({
                    tuguas.value = it
                    refreshState.value = NetworkState.LOADED
                }, { refreshState.value = NetworkState.error(it.message) }))
    }

    class TuGuaFactory @Inject
    constructor(private val mAppService: IAppService, private val mAppDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TuGuaViewModel(mAppService, mAppDatabase) as T
        }
    }

    private fun articlesEntityWrap(tuguas: List<TuGua>?): List<ArticleEntity> {
        return tuguas?.map { ArticleEntity.tuguaWrap(it) } ?: emptyList()
    }

    private fun tuguaWrap(articles: List<ArticleEntity>?): List<TuGua> {
        return articles?.map { TuGua(it) } ?: emptyList()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}