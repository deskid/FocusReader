package com.github.deskid.focusreader.utils

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Flowable<T>.asyncMain(): Flowable<T> {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}