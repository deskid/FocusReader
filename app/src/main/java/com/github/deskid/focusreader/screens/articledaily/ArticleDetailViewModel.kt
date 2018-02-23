package com.github.deskid.focusreader.screens.articledaily

import android.app.Application
import com.github.deskid.focusreader.api.data.Article
import com.github.deskid.focusreader.api.data.UIState.*
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ArticleDetailViewModel(application: Application) : BaseViewModel<Article>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    fun load(type: String) {
        disposable.add(appService.getArticle(type)
                .map {
                    it.apply {
                        data.content = data.content.replace("<p>", "<p>　　")
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = LoadingState() })
                .subscribe({
                    data.value = it
                    refreshState.value = LoadedState()
                }, { refreshState.value = ErrorState(it.message) }))
    }
}
