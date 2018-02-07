package com.github.deskid.focusreader.repository.tugua

import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.api.service.IAppService
import com.github.deskid.focusreader.db.AppDatabase
import io.reactivex.Observable
import javax.inject.Inject

class TuGuaRepository @Inject

constructor(private val appService: IAppService, private val appDatabase: AppDatabase) : TuGuaDataSource {

    override fun getTuGua(page: Int): Observable<PentiResource<List<TuGua>>> {
        return TuGuaRemoteDataSource(appService).getTuGua(page)
    }
}