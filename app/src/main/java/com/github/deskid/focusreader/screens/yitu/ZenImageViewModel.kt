package com.github.deskid.focusreader.screens.yitu

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.Resource
import com.github.deskid.focusreader.api.data.ZenImage
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.ArticleEntity
import com.github.deskid.focusreader.db.entity.YituEntity
import com.github.deskid.focusreader.utils.map
import com.github.deskid.focusreader.utils.transaction
import org.jetbrains.anko.doAsync
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import javax.inject.Inject

class ZenImageViewModel @Inject
constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModel() {

    fun load(page: Int = 1): LiveData<List<ZenImage>> {
        val result = MediatorLiveData<List<ZenImage>>()

        val dbSource: LiveData<List<ZenImage>> = appDatabase.articleDao().findArticleByType(1, page).map {
            if (it != null && !it.isEmpty()) {
                return@map zenImageWrap(it)
            } else {
                return@map emptyList<ZenImage>()
            }
        }

        val netWorkSource = appService.getZenImage(page).map { response ->
            if (response.code !in 200..300) {
                return@map emptyList<ZenImage>()
            } else {
                response.data?.data?.forEach {
                    zenImage ->
//                    zenImage.imgurl = zenImage.imgurl.replace("square", "large")
                    zenImage.url = zenImage.description
                }

                doAsync {
                    appDatabase.transaction {
                        appDatabase.articleDao().insertAll(articlesEntityWrap(response.data?.data))
                    }
                }

                return@map response.data?.data
            }
        }

        result.addSource(netWorkSource) {
            if (it != null && !it.isEmpty()) {
                result.value = it
            } else {
                result.removeSource(netWorkSource)
                result.addSource(dbSource) {
                    result.value = it
                }
            }
        }

        return result
    }

    fun loadZenImageDetail(url: String): LiveData<YituEntity> {
        val result = MediatorLiveData<YituEntity>()
        val dbSource = appDatabase.yituDao().findContentByUrl(url).map {
            if (it == null || it.isEmpty()) {
                return@map null
            } else {
                return@map it[0]
            }

        }
        val networkSource = appService.get(url).map {
            if (it.code !in 200..300 || it.data == null) {
                return@map Resource.error("network error with {$url}", null)
            }

            it.data?.let {
                val content = clean(it.string())
                val yituEntity = YituEntity(0, content, url)
                doAsync {
                    appDatabase.transaction {
                        appDatabase.yituDao().insert(yituEntity)
                    }
                }

                return@map Resource.success(yituEntity)
            }
        }

        result.addSource(dbSource) {
            if (it == null) {
                result.removeSource(dbSource)
                result.addSource(networkSource) {
                    if (it?.data != null) {
                        result.value = it.data
                    }
                }
            } else {
                result.value = it
            }
        }
        return result
    }

    private fun clean(string: String?): String {
        var result = string ?: ""
        result = result.replace(Regex("<title>.+?</title>"), "")
        result = Jsoup.clean(result, Whitelist.none())
        result = Jsoup.parse(result).text()
        return result
    }

    class ZenImageFactory @Inject
    constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ZenImageViewModel(appService, appDatabase) as T
        }
    }

    private fun articlesEntityWrap(zenImages: List<ZenImage>?): List<ArticleEntity> {
        return zenImages?.map { ArticleEntity.zenImageWrap(it) } ?: emptyList()
    }

    private fun zenImageWrap(articles: List<ArticleEntity>?): List<ZenImage> {
        return articles?.map { ZenImage(it) } ?: emptyList()
    }

}