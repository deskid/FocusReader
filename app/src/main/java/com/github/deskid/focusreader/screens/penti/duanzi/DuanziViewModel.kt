package com.github.deskid.focusreader.screens.penti.duanzi

import android.app.Application
import android.arch.lifecycle.LiveData
import com.github.deskid.focusreader.api.data.Duanzi
import com.github.deskid.focusreader.api.data.UIState.ErrorState
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.ArticleEntity
import com.github.deskid.focusreader.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

class DuanziViewModel(application: Application) : BaseViewModel<List<Duanzi>>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private var mPage: Int = 1

    override fun getLiveData(): LiveData<List<Duanzi>?> {
        return appDatabase.articleDao().findArticleByType(3, mPage).map {
            it.map { Duanzi(it) }
        }
    }

    fun load(page: Int = 1) {
        mPage = page

        disposable.add(appService.getJoke(page)
                .map { response ->
                    when {
                        response.error > 0 -> {
                            refreshState.value = ErrorState(response.msg)
                            emptyList()
                        }
                        else -> {
                            response.data?.forEach { duanzi ->
                                duanzi.description = clean(duanzi.description)
                                duanzi.title = duanzi.title.replace(Regex("【.+?】"), "")
                            }
                            response.data
                        }
                    }
                }
                .doOnNext { appDatabase.articleDao().insertAll(articlesEntityWrap(it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .subscribe(onLoaded, onError))
    }

    private fun articlesEntityWrap(duanzis: List<Duanzi>?): List<ArticleEntity> {
        return duanzis?.map { ArticleEntity.duanziWrap(it) } ?: emptyList()
    }

    private fun clean(string: String?): String {
        var result = string ?: ""
        result = Jsoup.clean(result, Whitelist.none())
        result = Jsoup.parse(result).text()
        return result
    }

}