package com.github.deskid.focusreader.utils

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations

fun <X, Y> LiveData<X>.map(transform: (x: X) -> Y): LiveData<Y> {
    return Transformations.map(this) {
        return@map transform(it)
    }
}

fun <X, Y> LiveData<X>.switchMap(transform: (x: X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this) {
        return@switchMap transform(it)
    }
}

fun <X, Y> MediatorLiveData<X>.map(transform: (x: X) -> Y): LiveData<Y> {
    return Transformations.map(this) {
        return@map transform(it)
    }
}

fun <X, Y> MediatorLiveData<X>.switchMap(transform: (x: X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this) {
        return@switchMap transform(it)
    }
}