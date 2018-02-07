package com.github.deskid.focusreader.activity

import android.arch.lifecycle.ViewModel
import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.api.service.IAppService
import io.reactivex.Flowable
import java.lang.IllegalStateException
import javax.inject.Inject

class SplashViewModel @Inject
constructor(private val appService: IAppService) : ViewModel() {
    fun splashImage(): Flowable<PentiResource<String>> {
        return appService.splashImage().map {
            if (it.error < 0) {
                throw IllegalStateException(it.msg)
            }
            return@map it
        }
    }
}
