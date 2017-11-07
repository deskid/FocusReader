package com.github.deskid.focusreader.screens.readhub.technews

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.data.Technews
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.utils.map
import javax.inject.Inject

class TechnewsViewModel @Inject
constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModel() {

    //todo add db cache logic
    fun load(): LiveData<Technews> {
        var result = MediatorLiveData<Technews>()

        var networkSource = appService.getReadhubTechnews(null).map {
            if (it.code in 200..300) {
                return@map it.data
            } else {
                return@map null
            }
        }

        result.addSource(networkSource) {
            result.value = it
        }

        return result
    }

    fun loadMore(lastCursor: Long): LiveData<Technews> {
        var result = MediatorLiveData<Technews>()

        var networkSource = appService.getReadhubTechnews(lastCursor).map {
            if (it.code in 200..300) {
                return@map it.data
            } else {
                return@map null
            }
        }

        result.addSource(networkSource) {
            result.value = it
        }

        return result
    }

    class Factory @Inject
    constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TechnewsViewModel(appService, appDatabase) as T
        }
    }
}