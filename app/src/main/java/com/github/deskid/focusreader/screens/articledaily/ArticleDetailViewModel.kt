package com.github.deskid.focusreader.screens.articledaily

import android.app.Application
import android.arch.lifecycle.LiveData
import com.github.deskid.focusreader.api.data.Article
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.DailyArticleEntity
import com.github.deskid.focusreader.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ArticleDetailViewModel(application: Application) : BaseViewModel<Article>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private var mType: String = ""

    override fun getData(): LiveData<Article?> {
        return appDatabase.dailyArticleDao().findArticleByType(mType, 1).map {
            when {
                it.isNotEmpty() -> return@map Article(it.first())
                else -> return@map null
            }
        }
    }

    fun load(type: String) {
        mType = type
        disposable.add(appService.getArticle(type)
                .map { it.apply { data.content.replace("<p>", "<p>　　") } }
                .doOnNext {
                    appDatabase.dailyArticleDao().insert(DailyArticleEntity(0, type,
                            it.data.title,
                            it.data.author,
                            it.data.content,
                            it.data.digest
                    ))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .subscribe(onLoaded, onError))
    }
}
