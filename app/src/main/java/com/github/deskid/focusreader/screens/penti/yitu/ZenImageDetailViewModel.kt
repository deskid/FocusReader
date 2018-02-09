package com.github.deskid.focusreader.screens.penti.yitu

import android.app.Application
import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.YituEntity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

class ZenImageDetailViewModel(application: Application) : BaseViewModel<YituEntity>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    fun loadZenImageDetail(url: String) {
        disposable.add(appDatabase.yituDao().findContentByUrl(url)
                .flatMap {
                    when {
                        it.isEmpty() -> appService.getContent(url).map {
                            val content = clean(it.string())
                            val yituEntity = YituEntity(0, content, url)
                            PentiResource.success(yituEntity)
                        }.doOnNext { appDatabase.yituDao().insert(it.data) }
                        else -> Flowable.just(PentiResource.success(it[0]))
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data.value = it.data })
    }

    private fun clean(string: String?): String {
        var result = string ?: ""
        result = result.replace(Regex("<title>.+?</title>"), "")
        result = Jsoup.clean(result, Whitelist.none())
        result = Jsoup.parse(result).text()
        return result
    }
}