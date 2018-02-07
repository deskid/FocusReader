package com.github.deskid.focusreader.screens.readhub.technews

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.data.NetworkState
import com.github.deskid.focusreader.api.data.Technews
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TechnewsViewModel @Inject
constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModel() {

    val technews: MutableLiveData<Technews> = MutableLiveData()
    val refreshState: MutableLiveData<NetworkState> = MutableLiveData()
    private val disposable: CompositeDisposable = CompositeDisposable()


    //todo add db cache logic
    fun load() {
        disposable.add(appService.getReadhubTechnews(null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = NetworkState.LOADING })
                .subscribe({
                    technews.value = it
                    refreshState.value = NetworkState.LOADED
                }, { refreshState.value = NetworkState.error(it.message) }))
    }

    fun loadMore(lastCursor: Long) {
        disposable.add(appService.getReadhubTechnews(lastCursor).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = NetworkState.LOADING })
                .subscribe({
                    technews.value = it
                    refreshState.value = NetworkState.LOADED
                }, { refreshState.value = NetworkState.error(it.message) }))
    }

    class Factory @Inject
    constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TechnewsViewModel(appService, appDatabase) as T
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}