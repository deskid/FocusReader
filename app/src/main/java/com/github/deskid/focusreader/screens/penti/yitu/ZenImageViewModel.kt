package com.github.deskid.focusreader.screens.penti.yitu

import android.app.Application
import com.github.deskid.focusreader.api.data.UIState.*
import com.github.deskid.focusreader.api.data.ZenImage
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.ArticleEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ZenImageViewModel(application: Application) : BaseViewModel<List<ZenImage>>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    fun load(page: Int = 1) {
        disposable.add(appService.getZenImage(page)
                .map { response ->
                    when {
                        response.error > 0 -> emptyList()
                        else -> {
                            response.data?.forEach { zenImage -> zenImage.url = zenImage.description }
                            response.data
                        }
                    }
                }
                .doOnNext { appDatabase.articleDao().insertAll(articlesEntityWrap(it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ refreshState.value = LoadingState() })
                .subscribe({
                    data.value = it
                    refreshState.value = LoadedState()
                }, { refreshState.value = ErrorState(it.message) }))
    }

    private fun articlesEntityWrap(zenImages: List<ZenImage>?): List<ArticleEntity> {
        return zenImages?.map { ArticleEntity.zenImageWrap(it) } ?: emptyList()
    }
}