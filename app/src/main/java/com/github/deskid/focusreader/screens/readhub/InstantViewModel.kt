package com.github.deskid.focusreader.screens.readhub

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.github.deskid.focusreader.api.data.InstantView
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.InstantContentEntity
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
        return Transformations.map(appDatabase.instantContentDao().query(mId)) {
            if (it.isNotEmpty()) {
                InstantView(it[0].url, it[0].title, it[0].content, it[0].siteName, it[0].siteSlug)
            } else {
                null
            }
        }
    }

    fun getContent(id: String) {
        mId = id
        disposable.add(appService.getReadhubInstantView(id)
                .map {
                    it.content = header + it.content + tail
                    it
                }
                .doOnNext {
                    appDatabase.instantContentDao().insert(
                            InstantContentEntity(
                                    id,
                                    it.content,
                                    it.url,
                                    it.title,
                                    it.siteName,
                                    it.siteSlug
                            ))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { refreshState.value = UIState.LoadingState() }
                .subscribe({
                    refreshState.value = UIState.LoadedState()
                }, { refreshState.value = UIState.ErrorState(it.message) }))
    }
}
