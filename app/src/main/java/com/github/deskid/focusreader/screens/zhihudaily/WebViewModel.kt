package com.github.deskid.focusreader.screens.zhihudaily

import android.app.Application
import com.github.deskid.focusreader.api.data.ErrorState
import com.github.deskid.focusreader.api.data.LoadedState
import com.github.deskid.focusreader.api.data.LoadingState
import com.github.deskid.focusreader.api.data.ZhihuDetail
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.ZhihuEntity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WebViewModel(application: Application) : BaseViewModel<ZhihuDetail>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    val header = """
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"  />
        <link rel="stylesheet" type="text/css" href="zhihu_style.css"/>
    </head>
    <body>"""

    val tail = """
    </body>
    </html>"""

    fun getContent(id: String) {
        disposable.add(appDatabase.zhihuDao().query(id).flatMap {
            when {
                it.isEmpty() -> appService.getZhihuDetail(id).map {
                    it.body = header + it.body + tail
                    it
                }.doOnNext { appDatabase.zhihuDao().insert(ZhihuEntity(id, it.title, it.body, it.image)) }
                else -> Flowable.just(ZhihuDetail(it[0].body, it[0].title, it[0].image))
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = LoadingState() })
                .subscribe({
                    data.value = it
                    refreshState.value = LoadedState()
                }, { refreshState.value = ErrorState(it.message) }))

    }
}
