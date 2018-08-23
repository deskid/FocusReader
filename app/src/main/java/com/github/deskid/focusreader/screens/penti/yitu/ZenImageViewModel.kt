package com.github.deskid.focusreader.screens.penti.yitu

import android.app.Application
import android.arch.lifecycle.LiveData
import com.github.deskid.focusreader.api.data.ZenImage
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.YituEntity
import com.github.deskid.focusreader.utils.map
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
        return appDatabase.yituDao().queryZenImage(mPage).map { it.map { ZenImage(it) } }
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
                }.doOnNext { insertEntities(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .subscribe(onLoaded, onError))

    }

    private fun insertEntities(zenImages: List<ZenImage>?) {
        zenImages?.map { zenImage ->
            appService.getContent(zenImage.url!!)
                    .map { makeYituEntity(zenImage, it.string()) }
                    .doOnNext { appDatabase.yituDao().insert(it) }
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

        return YituEntity(0, content, url, imageUrl, title, zenImage.description, zenImage.author, zenImage.pubDate)
    }
}