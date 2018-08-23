package com.github.deskid.focusreader.screens.readhub

import android.app.Application
import android.arch.lifecycle.LiveData
import com.github.deskid.focusreader.api.data.InstantView
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.InstantViewEntity
import com.github.deskid.focusreader.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class InstantViewModel(application: Application) : BaseViewModel<InstantView>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private var mId: String = ""

    val header = """
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"  />
                <link rel="stylesheet" type="text/css" href="readhub_style.css"/>

    </head>
    <body>"""

    val tail = """
    </body>
    </html>"""

    override fun getLiveData(): LiveData<InstantView?> {
        return appDatabase.instantContentDao().query(mId).map {
            when {
                it.isNotEmpty() -> InstantView(it[0])
                else -> null
            }
        }
    }

    fun getContent(id: String) {
        mId = id
        disposable.add(appService.getReadhubInstantView(id)
                .map { it.apply { content = header + content + tail } }
                .doOnNext {
                    appDatabase.instantContentDao().insert(InstantViewEntity(id, it))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .subscribe(onLoaded, onError))
    }
}
