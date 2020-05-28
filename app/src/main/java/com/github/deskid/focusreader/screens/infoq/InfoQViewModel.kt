package com.github.deskid.focusreader.screens.infoq

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.github.deskid.focusreader.api.data.InfoRequest
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.base.BaseViewModel
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.InfoqArticleEntity
import com.github.deskid.focusreader.utils.PagingRequestHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class InfoQViewModel(application: Application) : BaseViewModel<PagedList<InfoqArticleEntity>>(application) {
    override fun inject(app: App) {
        app.appComponent().inject(this)
    }

    private val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()

    private lateinit var sourceFactory: DataSource.Factory<Int, InfoqArticleEntity>
    private lateinit var dataSource: DataSource<Int, InfoqArticleEntity>

    override fun getData(): LiveData<PagedList<InfoqArticleEntity>?> {
        sourceFactory = appDatabase.infoqArticleDao().posts()
        dataSource = sourceFactory.create()
        return initializedPagedListBuilder(config).build()
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, InfoqArticleEntity> {

        val livePageListBuilder = LivePagedListBuilder<Int, InfoqArticleEntity>(
                sourceFactory,
                config)
        livePageListBuilder.setBoundaryCallback(InfoqBoundaryCallback(appDatabase, appService))
        return livePageListBuilder
    }

    fun load() {
        appService.getInfoQ(InfoRequest(null))
                .subscribeOn(Schedulers.io())
                .doOnNext { appDatabase.infoqArticleDao().insertAll(InfoqArticleEntity.wrap(it)) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(onLoading)
                .subscribe({
                    onLoaded(null)
                    dataSource.invalidate()
                }, {
                    onError(it)
                })
    }

    inner class InfoqBoundaryCallback(private val db: AppDatabase,
                                      private val api: IAppService) :
            PagedList.BoundaryCallback<InfoqArticleEntity>() {

        private val executor = Executors.newSingleThreadExecutor()
        private val helper = PagingRequestHelper(executor)

        override fun onZeroItemsLoaded() {
            super.onZeroItemsLoaded()
            helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { helperCallback ->
                api.getInfoQ(InfoRequest(null))
                        .subscribeOn(Schedulers.io())
                        .doOnNext { db.infoqArticleDao().insertAll(InfoqArticleEntity.wrap(it)) }
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

        override fun onItemAtEndLoaded(itemAtEnd: InfoqArticleEntity) {
            super.onItemAtEndLoaded(itemAtEnd)
            helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->
                api.getInfoQ(InfoRequest(itemAtEnd.score))
                        .subscribeOn(Schedulers.io())
                        .doOnNext { db.infoqArticleDao().insertAll(InfoqArticleEntity.wrap(it)) }
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
