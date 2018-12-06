package com.github.deskid.focusreader.screens.penti.yitu

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
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

    override fun getData(page: Int): LiveData<List<ZenImage>?> {
        val result = MediatorLiveData<List<ZenImage>>()

        result.addSource(appDatabase.yituDao().queryZenImage(page).map { it.map { ZenImage(it) } }) {
            result.value = it
        }
        data = result
        return data
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
                }.doOnNext { insertEntities(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .doOnComplete { currentPage.value = page }
                .subscribe(onLoaded, onError))

    }

    private fun insertEntities(zenImages: List<ZenImage>?) {
        zenImages?.map { zenImage ->
            val observer: Observer<List<YituEntity>> = Observer { result ->
                if (result == null || result.isEmpty()) {
                    appService.getContent(zenImage.url!!)
                            .map { makeYituEntity(zenImage, it.string()) }
                            .doOnNext { appDatabase.yituDao().insert(it) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {}
                }
            }
            appDatabase.yituDao().findContentByUrl(zenImage.url).observeForever(observer)
        }
    }

    private fun makeYituEntity(zenImage: ZenImage, string: String?): YituEntity {
        var content = string ?: ""
        val doc = Jsoup.parse(string)
        val imageUrl = doc.select("[src]").first().attr("abs:src")
        val title = Jsoup.clean(zenImage.title, Whitelist.none())
        val url = zenImage.url

        var id = 0
        val regex = Regex("&id=(.*)")
        if (regex.containsMatchIn(zenImage.url!!)) {
            id = regex.find(zenImage.url!!)!!.groupValues.last().toInt()
        }

        content = content.replace(Regex("<title>.+?</title>"), "")
        content = Jsoup.clean(content, Whitelist.none())
        content = Jsoup.parse(content).text()

        return YituEntity(id, content, url, imageUrl, title, zenImage.description, zenImage.author, zenImage.pubDate)
    }
}