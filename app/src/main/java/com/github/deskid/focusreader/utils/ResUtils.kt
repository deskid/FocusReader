package com.github.deskid.focusreader.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by zhoubo on 19/05/2017.
 */

object ResUtils {

    private val sMetrics = Resources.getSystem().displayMetrics

    fun dp2Px(dip: Float): Int {
        return dp2Px(dip.toInt())
    }

    fun dp2Px(dip: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), sMetrics).toInt()
    }

    fun screenWidth(): Int {
        return sMetrics.widthPixels
    }

    fun screenHeight(): Int {
        return sMetrics.heightPixels
    }
}

val Resources.screenHeight: Int
    get() = Resources.getSystem().displayMetrics.heightPixels

val Resources.screenWidth: Int
    get() = Resources.getSystem().displayMetrics.widthPixels
