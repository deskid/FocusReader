package com.github.deskid.focusreader.screens.readhub.news

import android.app.Application
import com.github.deskid.focusreader.api.data.News
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsViewModel(application: Application) : BaseViewModel<News>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    fun load(lastCursor: Long? = null) {
        disposable.add(appService.getReadhubNews(lastCursor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = UIState.LoadingState() })
                .subscribe({
                    data.value = it
                    refreshState.value = UIState.LoadedState()
                }, { refreshState.value = UIState.ErrorState(it.message) }))
    }
}