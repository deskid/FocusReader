package com.github.deskid.focusreader.screens.zhihudaily

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.deskid.focusreader.api.data.ZhihuDetail
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.utils.map
import javax.inject.Inject

class WebViewModel @Inject
constructor(val appService: IAppService, val appDatabase: AppDatabase) : ViewModel() {
    val header = """
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"  />
        <link rel="stylesheet" type="text/css" href="zhihu_style.css"/>
    </head>
    <body>"""

    val tail = """
    </body>
    </html>"""

    fun getContent(id: String): LiveData<ZhihuDetail> {
        val result = MediatorLiveData<ZhihuDetail>()
        val networkSource = appService.getZhihuDetail(id).map {
            if (it.code in 200..300) {
                it.data?.let {
                    it.body = header + it.body.replace("<div class=\"img-place-holder\">", "") + tail
                }
                return@map it.data
            } else {
                return@map null
            }
        }
        result.addSource(networkSource) {
            result.value = it
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
