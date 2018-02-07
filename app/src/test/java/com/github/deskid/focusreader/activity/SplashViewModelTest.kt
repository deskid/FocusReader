package com.github.deskid.focusreader.activity

import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.api.service.IAppService
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito

class SplashViewModelTest {
    @Test
    fun splashImage() {
        //given
        var appService = Mockito.mock(IAppService::class.java)
        given(appService.splashImage()).willReturn(Flowable.just(PentiResource.success("success")))
        val errors = TestSubscriber<PentiResource<String>>()
        //when
        SplashViewModel(appService).splashImage().subscribe(errors)

        //then
        errors.assertValue {
            it.data === "success"
        }

    }

    @Test
    fun `splashImage failure`() {
        //given
        var appService = Mockito.mock(IAppService::class.java)
        given(appService.splashImage()).willReturn(Flowable.just(PentiResource.error("fail", "")))
        val errors = TestSubscriber<PentiResource<String>>()
        //when
        SplashViewModel(appService).splashImage().subscribe(errors)

        //then
        errors.assertError {
            it.message == "fail"
        }

    }

}