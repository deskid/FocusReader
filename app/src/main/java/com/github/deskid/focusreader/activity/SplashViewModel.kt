package com.github.deskid.focusreader.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.Resource
import com.github.deskid.focusreader.api.service.IAppService
import javax.inject.Inject

class SplashViewModel @Inject
constructor(appService: IAppService) : ViewModel() {

    val splashImage: LiveData<Resource<String>>

    init {
        splashImage = Transformations.map(appService.splashImage()) { input -> input.data }
    }

    class Factory @Inject
    constructor(private val mAppService: IAppService) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel(mAppService) as T
        }
    }
}
