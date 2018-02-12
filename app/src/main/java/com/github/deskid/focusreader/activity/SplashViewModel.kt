package com.github.deskid.focusreader.activity

import android.arch.lifecycle.ViewModel
import com.github.deskid.focusreader.api.service.IAppService
import io.reactivex.Flowable
import javax.inject.Inject

class SplashViewModel @Inject
constructor(private val appService: IAppService) : ViewModel() {
    fun splashImage(): Flowable<String> {
        return appService.splash500pxImage().map {
            if (it.photos.isEmpty()) {
                return@map ""
            }
            return@map it.photos[0].image_url
        }
    }
}
