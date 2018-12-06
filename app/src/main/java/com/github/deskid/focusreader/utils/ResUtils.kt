package com.github.deskid.focusreader.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue

fun Context?.dp2Px(dip: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), Resources.getSystem().displayMetrics).toInt()
}

fun Context.dp2Px(dip: Float): Int {
    return dp2Px(dip.toInt())
}

val Context.screenHeight: Int
    get() = Resources.getSystem().displayMetrics.heightPixels

val Context.screenWidth: Int
    get() = Resources.getSystem().displayMetrics.widthPixels

@Suppress("DEPRECATION")
fun Context?.getColorCompat(id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this!!.getColor(id)
    } else {
        this!!.resources.getColor(id)
    }
}