package com.github.deskid.focusreader.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.db.AppDatabase
import io.reactivex.disposables.CompositeDisposable
import org.reactivestreams.Subscription
import java.net.UnknownHostException
import javax.inject.Inject

abstract class BaseViewModel<T>(application: Application) : AndroidViewModel(application) {

    abstract fun inject(app: App)

    @Inject
    lateinit var appService: IAppService

    @Inject
    lateinit var appDatabase: AppDatabase

    val refreshState: MutableLiveData<UIState> = MutableLiveData()

    val disposable: CompositeDisposable = CompositeDisposable()

    protected var currentPage: MutableLiveData<Int> = MutableLiveData()

    protected var data: MutableLiveData<T?> = MediatorLiveData()

    init {
        this.inject(this.getApplication())
        currentPage.value = 1
    }

    open fun getCurrentPage(): LiveData<Int> {
        return currentPage
    }

    open fun getData(page: Int): LiveData<T?> {
        return data
    }

    open fun getData(): LiveData<T?> {
        return data
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    val onError: (it: Throwable) -> Unit =  {
        when (it) {
            is UnknownHostException -> refreshState.value = UIState.NetworkErrorState()
            else -> refreshState.value = UIState.ErrorState(it.message)
        }
    }

    val onLoading: (Subscription) -> Unit = {
        refreshState.value = UIState.LoadingState()
    }

    val onLoaded: (T?) -> Unit = { refreshState.value = UIState.LoadedState() }

}
