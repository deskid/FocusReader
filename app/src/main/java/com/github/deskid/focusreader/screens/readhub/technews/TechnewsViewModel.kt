package com.github.deskid.focusreader.screens.readhub.technews

import android.app.Application
import android.arch.lifecycle.MutableLiveData
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
                .doOnSubscribe(onLoading)
                .subscribe({
                    (getData() as MutableLiveData).value = it
                    onLoaded(it)
                }, onError))
    }
}