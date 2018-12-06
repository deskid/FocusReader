package com.github.deskid.focusreader.screens.penti.tugua

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
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

    override fun getData(page: Int): LiveData<List<TuGua>?> {
        val result = MediatorLiveData<List<TuGua>>()
        result.addSource(appDatabase.articleDao().findArticleByType(2, page).map { tuguaList -> tuguaList.map { TuGua(it) } }) {
            result.value = it
        }
        data = result
        return data
    }

    fun load(page: Int = 1) {
        disposable.add(appService.getTuGua(page)
                .doOnNext {
                    if (it.error > 0) {
                        refreshState.value = ErrorState(it.msg)
                    }
                }
                .filter {
                    return@filter it.error == 0
                }
                .map { response ->
                    response.data = response.data?.filter {
                        !it.title.contentEquals("AD")
                    }
                    response.data?.forEach {
                        val regex = Regex("&id=(.*)")
                        if (regex.containsMatchIn(it.description)) {
                            it.id = regex.find(it.description)!!.groupValues.last().toInt()
                        }
                    }
                    response.data
                }.doOnNext {
                    it?.map {
                        appDatabase.articleDao().insert(ArticleEntity.tuguaWrap(it))
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { currentPage.value = page }
                .doOnSubscribe(onLoading)
                .subscribe(onLoaded, onError))
    }
}