package com.github.deskid.focusreader.screens.zhihudaily

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.data.Zhihu
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.utils.map
import javax.inject.Inject

class ZhihuViewModel @Inject
constructor(val appService: IAppService, val appDatabase: AppDatabase) : ViewModel() {

    //todo add db cache logic
    fun load(): LiveData<Zhihu> {
        var result = MediatorLiveData<Zhihu>()

        var networkSource = appService.getZhihuLatest().map {
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

    fun loadMore(date: String): LiveData<Zhihu> {
        var result = MediatorLiveData<Zhihu>()

        var networkSource = appService.getZhihuHistory(date).map {
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

    class ZhihuFactory @Inject
    constructor(private val appService: IAppService, val appDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ZhihuViewModel(appService, appDatabase) as T
        }
    }
}