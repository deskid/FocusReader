package com.github.deskid.focusreader.utils

import android.content.Context
import android.net.ConnectivityManager

fun Context.isNetworkAvailable(): Boolean {
    val service = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return service.activeNetworkInfo?.isConnected ?: false
}