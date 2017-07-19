package com.github.deskid.focusreader.activity

import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.WebContentEntity
import com.github.deskid.focusreader.utils.asyncMain
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject

class WebViewModel @Inject
constructor(val appService: IAppService, val appDatabase: AppDatabase) {
    fun getContent(url: String): Flowable<String> {
        return appDatabase.webContentDao().findContentByUrl(url).flatMap { webContent ->
            if (webContent.isNotEmpty()) {
                return@flatMap Flowable.create<String>({
                    it.onNext(webContent[0].content)
                    it.onComplete()
                }, BackpressureStrategy.BUFFER).asyncMain()
            } else {
                return@flatMap appService.get(url).map {
                    val content = filter(it.string())
                    appDatabase.webContentDao().insert(WebContentEntity(0, 0, content, url))
                    return@map content
                }.asyncMain()
            }
        }

    }

    fun filter(oldContent: String): String {
        val header = """<link rel="stylesheet" type="text/css" href="content_style.css"/>"""
        return header + oldContent
    }

}
