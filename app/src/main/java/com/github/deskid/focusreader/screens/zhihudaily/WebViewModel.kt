package com.github.deskid.focusreader.screens.zhihudaily

import android.app.Application
import android.arch.lifecycle.LiveData
import com.github.deskid.focusreader.api.data.ZhihuDetail
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.ZhihuEntity
import com.github.deskid.focusreader.utils.map
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

    private var mId = ""

    override fun getData(): LiveData<ZhihuDetail?> {
        return appDatabase.zhihuDao().query(mId).map {
            when {
                it.isNotEmpty() -> return@map ZhihuDetail(it.first())
                else -> return@map null
            }
        }
    }

    fun getContent(id: String) {
        mId = id
        disposable.add(appService.getZhihuDetail(id).map { it.apply { body = header + body + tail } }
                .doOnNext { appDatabase.zhihuDao().insert(ZhihuEntity(id, it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .subscribe(onLoaded, onError))
    }
}
