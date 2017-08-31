package com.github.deskid.focusreader.screens.duanzi

import android.arch.lifecycle.*
import com.github.deskid.focusreader.api.Resource
import com.github.deskid.focusreader.api.data.Duanzi
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.ArticleEntity
import com.github.deskid.focusreader.utils.transaction
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class DuanziViewModel @Inject
constructor(appService: IAppService, appDatabase: AppDatabase) : ViewModel() {

    private val mAppService: IAppService = appService
    private val mAppDatabase: AppDatabase = appDatabase

    fun load(page: Int = 1): LiveData<Resource<List<Duanzi>>> {
        val result = MediatorLiveData<Resource<List<Duanzi>>>()
        val dbSource = Transformations.map(mAppDatabase.articleDao().findArticleByType(3, page)) {
            Resource.success(duanziWrap(it))
        }
        val netWorkSource = mAppService.getJoke(page)
        result.addSource(netWorkSource) {
            if (it == null || it.code !in 200..300) {
                result.removeSource(netWorkSource)
                result.addSource(dbSource) {
                    result.value = it
                }
            } else if (it.code in 200..300) {
                doAsync {
                    mAppDatabase.transaction {
                        mAppDatabase.articleDao().insertAll(articleEntityWrap(it.data?.data))
                    }
                }
                result.value = it.data
            }
        }
        return result
    }

    class JokeFactory @Inject
    constructor(private val mAppService: IAppService, private val mAppDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DuanziViewModel(mAppService, mAppDatabase) as T
        }
    }

    private fun articleEntityWrap(duanzis: List<Duanzi>?): List<ArticleEntity> {
        return duanzis?.map { ArticleEntity.duanziWrap(it) } ?: emptyList()
    }

    private fun duanziWrap(articles: List<ArticleEntity>?): List<Duanzi> {
        return articles?.map { Duanzi(it) } ?: emptyList()
    }

}