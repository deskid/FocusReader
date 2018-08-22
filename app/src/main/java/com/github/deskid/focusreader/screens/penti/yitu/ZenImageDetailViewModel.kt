package com.github.deskid.focusreader.screens.penti.yitu

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.entity.YituEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

class ZenImageDetailViewModel(application: Application) : BaseViewModel<YituEntity>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private var mUrl: String = ""

    override fun getLiveData(): LiveData<YituEntity?> {
        return Transformations.map(appDatabase.yituDao().findContentByUrl(mUrl)) {
            it[0]
        }
    }

    fun loadZenImageDetail(url: String) {
        mUrl = url
        disposable.add(appService.getContent(url)
                .map {
                    val yituEntity = makeYituEntity(url, it.string())
                    PentiResource.success(yituEntity)
                }.doOnNext { appDatabase.yituDao().insert(it.data) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { refreshState.value = UIState.LoadingState() }
                .subscribe({
                    refreshState.value = UIState.LoadedState()
                }, { refreshState.value = UIState.ErrorState(it.message) }))

    }

    private fun makeYituEntity(url: String, string: String?): YituEntity {
        var content = string ?: ""
        val doc = Jsoup.parse(string)
        val imageUrl = doc.select("[src]").first().attr("abs:src")
        val title = doc.title().replace("--喷嚏网 dapenti.com", "")

        content = content.replace(Regex("<title>.+?</title>"), "")
        content = Jsoup.clean(content, Whitelist.none())
        content = Jsoup.parse(content).text()

        return YituEntity(0, content, url, imageUrl, title)
    }
}