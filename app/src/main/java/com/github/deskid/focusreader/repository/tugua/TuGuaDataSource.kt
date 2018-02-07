package com.github.deskid.focusreader.repository.tugua

import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.api.data.TuGua
import io.reactivex.Observable

interface TuGuaDataSource {
    fun getTuGua(page: Int): Observable<PentiResource<List<TuGua>>>
}