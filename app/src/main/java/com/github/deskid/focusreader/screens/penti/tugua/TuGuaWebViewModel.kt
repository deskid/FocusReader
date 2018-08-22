package com.github.deskid.focusreader.screens.penti.tugua

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.github.deskid.focusreader.api.data.UIState.*
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.WebContentEntity
import com.github.logutils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TuGuaWebViewModel(application: Application) : BaseViewModel<String>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private var mUrl: String = ""

    val header = """
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta name="viewport" content="width=device-width, height=device-height, initial-scale=1, maximum-scale=1, user-scalable=no">
        <link rel="stylesheet" type="text/css" href="content_style.css"/>
    </head>
    <body>"""

    val tail = """
    </body>
    </html>"""

    override fun getLiveData(): LiveData<String?> {
        return Transformations.map(appDatabase.webContentDao().query(mUrl)) {
            if (it.isNotEmpty()) {
                it[0].content
            } else {
                null
            }
        }
    }

    fun getContent(url: String) {
        mUrl = url
        disposable.add(appService.getContent(url)
                .map { mixStyle(it.string()) }
                .doOnNext { appDatabase.webContentDao().insert(WebContentEntity(0, 0, it, url)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { refreshState.value = LoadingState() }
                .subscribe({
                    refreshState.value = LoadedState()
                }, { refreshState.value = ErrorState(it.message) }))

    }

    // viewport sometimes has bug
    private fun mixStyle(content: String): String {
        val HTML_TAG = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"  />"

        val index = content.indexOf(HTML_TAG) + HTML_TAG.length
        val result = header + content.substring(index + 1) + tail
        LogUtils.d(result)
        return result
    }
}
