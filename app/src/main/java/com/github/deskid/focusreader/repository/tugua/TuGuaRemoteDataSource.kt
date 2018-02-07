package com.github.deskid.focusreader.repository.tugua

import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.api.service.IAppService
import io.reactivex.Observable
import javax.inject.Inject

class TuGuaRemoteDataSource @Inject
constructor(private val appService: IAppService) : TuGuaDataSource {
    override fun getTuGua(page: Int): Observable<PentiResource<List<TuGua>>> {
        return Observable.just(PentiResource.success(emptyList()))
    }
}