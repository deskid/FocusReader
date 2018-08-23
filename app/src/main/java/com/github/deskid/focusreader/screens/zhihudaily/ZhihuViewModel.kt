package com.github.deskid.focusreader.screens.zhihudaily

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.github.deskid.focusreader.api.data.Zhihu
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ZhihuViewModel(application: Application) : BaseViewModel<Zhihu>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    fun load() {
        disposable.add(appService.getZhihuLatest()
                .map {
                    it.stories = ArrayList(it.stories.filter { !it.title.contentEquals("这里是广告") })
                    it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .subscribe({
                    (getLiveData() as MutableLiveData).value = it
                    onLoaded(it)
                }, onError))
    }

    fun loadMore(date: String) {
        disposable.add(appService.getZhihuHistory(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .subscribe({
                    (getLiveData() as MutableLiveData).value = it
                    onLoaded(it)
                }, onError))
    }
}