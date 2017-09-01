package com.github.deskid.focusreader.screens.tugua

import android.arch.lifecycle.*
import com.github.deskid.focusreader.api.Resource
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.ArticleEntity
import com.github.deskid.focusreader.utils.transaction
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class TuGuaViewModel @Inject
constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModel() {

    fun load(page: Int = 1): LiveData<Resource<List<TuGua>>> {
        val result = MediatorLiveData<Resource<List<TuGua>>>()
        val dbSource = Transformations.map(appDatabase.articleDao().findArticleByType(2, page)) {
            Resource.success(tuguaWrap(it))
        }

        val netWorkSource = appService.getTuGua(page)
        result.addSource(netWorkSource) {
            if (it == null || it.code !in 200..300) {
                result.removeSource(netWorkSource)
                result.addSource(dbSource) {
                    result.value = it
                }
            } else if (it.code in 200..300) {

                val value = it.data?.apply {
                    data = data?.filter { !it.title.contentEquals("AD") }
                }

                doAsync {
                    appDatabase.transaction {
                        appDatabase.articleDao().insertAll(articleEntityWrap(value?.data))
                    }
                }
                result.value = value
            }
        }
        return result
    }

    class TuGuaFactory @Inject
    constructor(private val mAppService: IAppService, private val mAppDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TuGuaViewModel(mAppService, mAppDatabase) as T
        }
    }

    private fun articleEntityWrap(tuguas: List<TuGua>?): List<ArticleEntity> {
        return tuguas?.map { ArticleEntity.tuguaWrap(it) } ?: emptyList()
    }

    private fun tuguaWrap(articles: List<ArticleEntity>?): List<TuGua> {
        return articles?.map { TuGua(it) } ?: emptyList()
    }

}