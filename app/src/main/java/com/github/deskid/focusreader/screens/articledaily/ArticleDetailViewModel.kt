package com.github.deskid.focusreader.screens.articledaily

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.data.Article
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.utils.map
import javax.inject.Inject

class ArticleDetailViewModel @Inject
constructor(private val appService: IAppService,private val appDatabase: AppDatabase) : ViewModel() {

    fun load(type: String): LiveData<Article> {
        val result = MediatorLiveData<Article>()
        val networkSource = appService.getArticle(type).map {
            it.data?.let {
                it.data.content = it.data.content.replace("<p>", "<p>　　")
            }
            return@map it.data
        }

        result.addSource(networkSource) {
            result.value = it
        }

        return result
    }

    class Factory @Inject
    constructor(private val mAppService: IAppService, private val mAppDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ArticleDetailViewModel(mAppService, mAppDatabase) as T
        }
    }
}
