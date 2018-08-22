package com.github.deskid.focusreader.screens.penti.tugua

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.api.data.UIState.*
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.ArticleEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TuGuaViewModel(application: Application) : BaseViewModel<List<TuGua>>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    fun load(page: Int = 1) {

//        data = appDatabase.articleDao().findArticleByType(2, page).map {
//            it.map { TuGua(it) }
//        }

        disposable.add(appService.getTuGua(page)
                .map { response ->
                    when {
                        response.error > 0 -> {
                            refreshState.value = ErrorState(response.msg)
                            emptyList()
                        }
                        else -> {
                            response.data = response.data?.filter {
                                !it.title.contentEquals("AD")
                            }
                            response.data
                        }
                    }
                }.doOnNext {
                    it?.map {
                        appDatabase.articleDao().insert(ArticleEntity.tuguaWrap(it))
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { refreshState.value = LoadingState() }
                .subscribe({
                    (getLiveData() as MutableLiveData).value = it
                    refreshState.value = LoadedState()
                }, { refreshState.value = ErrorState(it.message) }))

    }
}