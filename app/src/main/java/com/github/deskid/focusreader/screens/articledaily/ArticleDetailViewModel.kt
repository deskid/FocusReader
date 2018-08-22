package com.github.deskid.focusreader.screens.articledaily

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.github.deskid.focusreader.api.data.Article
import com.github.deskid.focusreader.api.data.Data
import com.github.deskid.focusreader.api.data.Date
import com.github.deskid.focusreader.api.data.UIState.*
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.DailyArticleEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ArticleDetailViewModel(application: Application) : BaseViewModel<Article>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private var mType: String = ""

    override fun getLiveData(): LiveData<Article?> {
        return Transformations.map(appDatabase.dailyArticleDao().findArticleByType(mType, 1)) {
            if (it.isNotEmpty()) {
                return@map Article(
                        Data(it.first().author,
                                it.first().content,
                                Date("", "", ""),
                                it.first().digest,
                                it.first().title
                        )
                )
            } else {
                return@map null
            }
        }
    }

    fun load(type: String) {
        mType = type
        disposable.add(appService.getArticle(type)
                .map {
                    it.apply {
                        data.content = data.content.replace("<p>", "<p>　　")
                    }
                }
                .doOnNext {
                    appDatabase.dailyArticleDao().insert(DailyArticleEntity(
                            0,
                            type,
                            it.data.title,
                            it.data.author,
                            it.data.content,
                            it.data.digest
                    ))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { refreshState.value = LoadingState() }
                .subscribe({
                    refreshState.value = LoadedState()
                }, { refreshState.value = ErrorState(it.message) }))
    }
}
