package com.github.deskid.focusreader.utils

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations

fun <X, Y> LiveData<X>.map(transform: (x: X) -> Y): LiveData<Y> {
    return Transformations.map(this) {
        transform(it)
    }
}

fun <X, Y> LiveData<X>.switchMap(transform: (x: X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this) {
        transform(it)
    }
}

fun <X, Y> MutableLiveData<X>.map(transform: (x: X) -> Y): LiveData<Y> {
    return Transformations.map(this) {
        transform(it)
    }
}

fun <X, Y> MutableLiveData<X>.switchMap(transform: (x: X) -> MutableLiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this) {
        transform(it)
    }
}

fun <X, Y> MediatorLiveData<X>.map(transform: (x: X) -> Y): LiveData<Y> {
    return Transformations.map(this) {
        transform(it)
    }
}

fun <X, Y> MediatorLiveData<X>.switchMap(transform: (x: X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this) {
        transform(it)
    }
}