package com.github.deskid.focusreader.repository.tugua

import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.db.AppDatabase
import com.github.deskid.focusreader.db.entity.ArticleEntity
import io.reactivex.Observable
import javax.inject.Inject

class TuGuaLocalDataSource @Inject
constructor(private val appDatabase: AppDatabase) : TuGuaDataSource {

    override fun getTuGua(page: Int): Observable<PentiResource<List<TuGua>>> {
        return Observable.just(PentiResource.success(emptyList()))
//        val result = MediatorLiveData<PentiResource<List<TuGua>>>()
//        val dbSource = Transformations.map(appDatabase.articleDao().findArticleByType(2, page)) {
//            PentiResource.success(tuguaWrap(it))
//        }
//
//        result.addSource(dbSource) {
//            result.value = it
//        }
//        return result
    }

    private fun tuguaWrap(articles: List<ArticleEntity>?): List<TuGua> {
        return articles?.map { TuGua(it) } ?: emptyList()
    }
}