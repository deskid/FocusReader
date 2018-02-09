package com.github.deskid.focusreader.screens.penti.tugua

import android.app.Application
import com.github.deskid.focusreader.api.data.ErrorState
import com.github.deskid.focusreader.api.data.LoadedState
import com.github.deskid.focusreader.api.data.LoadingState
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.WebContentEntity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TuGuaWebViewModel(application: Application) : BaseViewModel<String>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private val header = """<link rel="stylesheet" type="text/css" href="content_style.css"/>"""

    fun getContent(url: String) {
        disposable.add(appDatabase.webContentDao().query(url).flatMap {
            when {
                it.isEmpty() -> appService.getContent(url)
                        .map { header + it.string() }
                        .doOnNext { appDatabase.webContentDao().insert(WebContentEntity(0, 0, it, url)) }
                else -> Flowable.just(it[0].content)
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
