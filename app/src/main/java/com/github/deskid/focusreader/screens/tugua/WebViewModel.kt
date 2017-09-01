package com.github.deskid.focusreader.screens.tugua

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.WebContentEntity
import com.github.deskid.focusreader.utils.map
import com.github.deskid.focusreader.utils.transaction
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class WebViewModel @Inject
constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : ViewModel() {
    private val header = """<link rel="stylesheet" type="text/css" href="content_style.css"/>"""

    fun getContent(url: String): LiveData<String> {

        val result = MediatorLiveData<String>()

        val dbSource = appDatabase.webContentDao().findContentByUrl(url).map {
            if (it.isEmpty()) {
                return@map ""
            } else {
                return@map it[0].content
            }
        }

        val networkSource = appService.get(url).map {
            if (it.code in 200..300) {
                val content = header + (it.data?.string() ?: "")

                doAsync {
                    appDatabase.transaction {
                        appDatabase.webContentDao().insert(WebContentEntity(0, 0, content, url))
                    }
                }
                return@map content
            } else {
                return@map ""
            }
        }

        result.addSource(dbSource) {
            if (it.isNullOrEmpty()) {
                result.removeSource(dbSource)
                result.addSource(networkSource) {
                    result.value = it
                }
            } else {
                result.value = it
            }
        }
        return result
    }

    class WebViewModelFactory @Inject
    constructor(private val mAppService: IAppService, private val mAppDatabase: AppDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return WebViewModel(mAppService, mAppDatabase) as T
        }
    }

}
