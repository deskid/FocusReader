package com.github.deskid.focusreader.screens.penti.yitu

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.YituEntity

class ZenImageDetailViewModel(application: Application) : BaseViewModel<YituEntity>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private var mUrl: String = ""

    override fun getData(): LiveData<YituEntity?> {
        return Transformations.map(appDatabase.yituDao().findContentByUrl(mUrl)) {
            it[0]
        }
    }

    fun loadZenImageDetail(url: String) {
        mUrl = url
    }
}