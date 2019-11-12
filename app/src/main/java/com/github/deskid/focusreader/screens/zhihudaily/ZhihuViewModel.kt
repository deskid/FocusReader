package com.github.deskid.focusreader.screens.zhihudaily

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.ZhihuDailyPostEntity
import com.github.deskid.focusreader.utils.PagingRequestHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class ZhihuViewModel(application: Application) : BaseViewModel<PagedList<ZhihuDailyPostEntity>>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()

    override fun getLiveData(): LiveData<PagedList<ZhihuDailyPostEntity>?> {
        return initializedPagedListBuilder(config).build()
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, ZhihuDailyPostEntity> {

        val database = appDatabase
        val livePageListBuilder = LivePagedListBuilder<Int, ZhihuDailyPostEntity>(
                database.zhihuDao().posts(),
                config)
        livePageListBuilder.setBoundaryCallback(ZhihuBoundaryCallback(database, appService))
        return livePageListBuilder
    }

    fun load() {
        onLoaded(null)
    }

    inner class ZhihuBoundaryCallback(private val db: AppDatabase,
                                      private val api: IAppService) :
            PagedList.BoundaryCallback<ZhihuDailyPostEntity>() {

        private val executor = Executors.newSingleThreadExecutor()
        private val helper = PagingRequestHelper(executor)

        override fun onZeroItemsLoaded() {
            super.onZeroItemsLoaded()
            helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { helperCallback ->
                api.getZhihuLatest()
                        .map {
                            it.stories = ArrayList(it.stories.filter { !it.title.contentEquals("这里是广告") })
                            it
                        }
                        .subscribeOn(Schedulers.io())
                        .doOnNext { db.zhihuDao().insert(ZhihuDailyPostEntity.zhihuWrap(it)) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(onLoading)
                        .subscribe({
                            onLoaded(null)
                            helperCallback.recordSuccess()
                        }, {
                            onError(it)
                            helperCallback.recordFailure(it)
                        })
            }
        }

        override fun onItemAtEndLoaded(itemAtEnd: ZhihuDailyPostEntity) {
            super.onItemAtEndLoaded(itemAtEnd)
            helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->
                api.getZhihuHistory(date = itemAtEnd.date)
                        .subscribeOn(Schedulers.io())
                        .doOnNext { db.zhihuDao().insert(ZhihuDailyPostEntity.zhihuWrap(it)) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(onLoading)
                        .subscribe({
                            onLoaded(null)
                            helperCallback.recordSuccess()
                        }, {
                            onError(it)
                            helperCallback.recordFailure(it)
                        })
            }
        }
    }
}