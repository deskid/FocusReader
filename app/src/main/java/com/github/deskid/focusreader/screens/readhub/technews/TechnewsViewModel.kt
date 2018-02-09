package com.github.deskid.focusreader.screens.readhub.technews

import android.app.Application
import com.github.deskid.focusreader.api.data.ErrorState
import com.github.deskid.focusreader.api.data.LoadedState
import com.github.deskid.focusreader.api.data.LoadingState
import com.github.deskid.focusreader.api.data.Technews
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TechnewsViewModel(application: Application) : BaseViewModel<Technews>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    fun load(lastCursor: Long? = null) {
        disposable.add(appService.getReadhubTechnews(lastCursor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = LoadingState() })
                .subscribe({
                    data.value = it
                    refreshState.value = LoadedState()
                }, { refreshState.value = ErrorState(it.message) }))
    }
}