package com.github.deskid.focusreader.screens.penti.yitu

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.api.data.UIState.ErrorState
import com.github.deskid.focusreader.api.data.ZenImage
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.YituEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

class ZenImageViewModel(application: Application) : BaseViewModel<List<ZenImage>>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private var mPage = 1

    override fun getLiveData(): LiveData<List<ZenImage>?> {
        return Transformations.map(appDatabase.yituDao().queryZenImage(mPage)) {
            it.map { ZenImage(it.title, it.content, it.imgurl, "", "", it.url) }
        }
    }

    fun load(page: Int = 1) {
        mPage = page

        disposable.add(appService.getZenImage(page)
                .map { response ->
                    when {
                        response.error > 0 -> emptyList()
                        else -> {
                            response.data?.forEach { zenImage -> zenImage.url = zenImage.description }
                            response.data
                        }
                    }
                }.doOnNext { insertArticlesEntity(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { refreshState.value = UIState.LoadingState() }
                .subscribe({
                    //                    data.value = it
                    refreshState.value = UIState.LoadedState()
                }, { refreshState.value = ErrorState(it.message) }))

    }

    private fun insertArticlesEntity(zenImages: List<ZenImage>?) {
        zenImages?.map { zenImage ->
            appService.getContent(zenImage.url!!)
                    .map { res ->
                        makeYituEntity(zenImage, res.string())
                    }
                    .doOnNext {
                        appDatabase.yituDao().insert(it)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {}
        }
    }

    private fun makeYituEntity(zenImage: ZenImage, string: String?): YituEntity {
        var content = string ?: ""
        val doc = Jsoup.parse(string)
        val imageUrl = doc.select("[src]").first().attr("abs:src")
        val title = zenImage.title
        val url = zenImage.url

        content = content.replace(Regex("<title>.+?</title>"), "")
        content = Jsoup.clean(content, Whitelist.none())
        content = Jsoup.parse(content).text()

        return YituEntity(0, content, url, imageUrl, title)
    }
}