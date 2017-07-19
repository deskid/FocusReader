package com.github.deskid.focusreader.screens.yitu

import android.arch.lifecycle.*
import com.github.deskid.focusreader.api.Resource
import com.github.deskid.focusreader.api.data.ZenImage
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.ArticleEntity
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ZenImageViewModel @Inject
constructor(val appService: IAppService, val appDatabase: AppDatabase) : ViewModel() {

    fun load(page: Int = 1): LiveData<Resource<List<ZenImage>>> {
        val result = MediatorLiveData<Resource<List<ZenImage>>>()
        val dbSource = Transformations.map(appDatabase.articleDao().findArticleByType(1, page)) {
            Resource.success(zenImageWrap(it))
        }

        val netWorkSource = appService.getZenImage(page)
        result.addSource(netWorkSource) {
            if (it == null || it.code !in 200..300) {
                result.removeSource(netWorkSource)
                result.addSource(dbSource) {
                    result.value = it
                }
            } else if (it.code in 200..300) {
                it.data?.apply {
                    data?.forEach {
                        it.imgurl = it.imgurl.replace("square", "medium")
                    }
                }
                Completable.fromAction {
                    appDatabase.articleDao().insertAll(articleEntityWrap(it.data?.data))
                }.subscribeOn(Schedulers.io()).subscribe()
                result.value = it.data
            }
        }
        return result

    }

    class ZenImageFactory @Inject
    constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ZenImageViewModel(appService, appDatabase) as T
        }
    }

    fun articleEntityWrap(zenImages: List<ZenImage>?): List<ArticleEntity> {
        return zenImages?.map { ArticleEntity.zenImageWrap(it) } ?: emptyList()
    }

    fun zenImageWrap(articles: List<ArticleEntity>?): List<ZenImage> {
        return articles?.map { ZenImage(it) } ?: emptyList()
    }

}