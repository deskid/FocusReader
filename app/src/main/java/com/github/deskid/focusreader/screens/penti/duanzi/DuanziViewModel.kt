package com.github.deskid.focusreader.screens.penti.duanzi

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
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

    override fun getData(page: Int): LiveData<List<Duanzi>?> {
        val result = MediatorLiveData<List<Duanzi>>()

        result.addSource(appDatabase.articleDao().findArticleByType(3, page).map {
            it.map { Duanzi(it) }
        }) {
            result.value = it
        }
        data = result
        return data

    }

    fun load(page: Int = 1) {
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
                                val regex = Regex("&id=(.*)")
                                if (regex.containsMatchIn(duanzi.link)) {
                                    duanzi.id = regex.find(duanzi.link)!!.groupValues.last().toInt()
                                }
                            }
                            response.data
                        }
                    }
                }
                .doOnNext { appDatabase.articleDao().insertAll(articlesEntityWrap(it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .doOnComplete { currentPage.value = page }
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