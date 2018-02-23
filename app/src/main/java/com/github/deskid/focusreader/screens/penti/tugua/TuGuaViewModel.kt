package com.github.deskid.focusreader.screens.penti.tugua

import android.app.Application
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.api.data.UIState.*
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.ArticleEntity
import com.github.logutils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TuGuaViewModel(application: Application) : BaseViewModel<List<TuGua>>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    fun load(page: Int = 1) {
        disposable.add(appService.getTuGua(page)
                .map { response ->
                    when {
                        response.error > 0 -> {
                            refreshState.value = ErrorState(response.msg)
                            emptyList()
                        }
                        else -> {
                            response.data?.filter { !it.title.contentEquals("AD") }
                        }
                    }
                }.doOnNext { appDatabase.articleDao().insertAll(articlesEntityWrap(it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = LoadingState() })
                .subscribe({
                    data.value = it
                    refreshState.value = LoadedState()
                }, {
                    LogUtils.logStackTrace(it)
                    refreshState.value = ErrorState(it.message)
                }))
    }

    private fun articlesEntityWrap(tuguas: List<TuGua>?): List<ArticleEntity> {
        return tuguas?.map { ArticleEntity.tuguaWrap(it) } ?: emptyList()
    }
}