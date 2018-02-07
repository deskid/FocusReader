package com.github.deskid.focusreader.screens.zhihudaily

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.data.NetworkState
import com.github.deskid.focusreader.api.data.Zhihu
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ZhihuViewModel @Inject
constructor(private val appService: IAppService,private val appDatabase: AppDatabase) : ViewModel() {

    val zhihuList: MutableLiveData<Zhihu> = MutableLiveData()
    val refreshState: MutableLiveData<NetworkState> = MutableLiveData()

    private val disposable: CompositeDisposable = CompositeDisposable()

    fun load() {
        disposable.add(appService.getZhihuLatest()
                .map {
                    it.stories = ArrayList(it.stories.filter { !it.title.contentEquals("这里是广告") })
                    it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = NetworkState.LOADING })
                .subscribe({
                    zhihuList.value = it
                    refreshState.value = NetworkState.LOADED
                }, { refreshState.value = NetworkState.error(it.message) }))
    }

    fun loadMore(date: String) {
        disposable.add(appService.getZhihuHistory(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = NetworkState.LOADING })
                .subscribe({
                    zhihuList.value = it
                    refreshState.value = NetworkState.LOADED
                }, { refreshState.value = NetworkState.error(it.message) }))
    }

    class ZhihuFactory @Inject
    constructor(private val appService: IAppService, val appDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ZhihuViewModel(appService, appDatabase) as T
        }
    }
}