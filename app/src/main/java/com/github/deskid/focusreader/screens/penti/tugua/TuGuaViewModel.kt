package com.github.deskid.focusreader.screens.penti.tugua

import android.app.Application
import android.arch.lifecycle.LiveData
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.api.data.UIState.ErrorState
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.ArticleEntity
import com.github.deskid.focusreader.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TuGuaViewModel(application: Application) : BaseViewModel<List<TuGua>>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private var mPage = 1

    override fun getLiveData(): LiveData<List<TuGua>?> {
        return appDatabase.articleDao().findArticleByType(2, mPage).map { tuguaList ->
            tuguaList.map { TuGua(it) }
        }
    }

    fun load(page: Int = 1) {
        mPage = page
        disposable.add(appService.getTuGua(page)
                .map { response ->
                    if (response.error > 0) {
                        refreshState.value = ErrorState(response.msg)
                        emptyList()
                    } else {
                        response.data = response.data?.filter {
                            !it.title.contentEquals("AD")
                        }
                        response.data?.forEach {
                            it.title.replace(Regex("【.+?】"), "")
                        }
                        response.data
                    }
                }.doOnNext {
                    it?.map {
                        appDatabase.articleDao().insert(ArticleEntity.tuguaWrap(it))
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .subscribe(onLoaded, onError))

    }
}