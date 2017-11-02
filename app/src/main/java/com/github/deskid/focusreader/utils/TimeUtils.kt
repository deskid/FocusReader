package com.github.deskid.focusreader.utils

import com.github.logutils.LogUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

//2017-10-27T08:20:14.935Z
fun String.toDate(format: DateFormat =
                  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.CHINA)
                          .apply { timeZone = TimeZone.getTimeZone("GMT") }): Date {
    return format.parse(this)
}

fun String.withoutSuffix(): String {
    return this.substring(0, this.lastIndex)
}

fun Date.fromNow(): String {
    val now = Date()
    val millis = now.time - time
    LogUtils.e("now.time ${now.time} : $time")
    LogUtils.e(millis.toString() + "----")
    val units = arrayOf("天", "小时", "分钟", "秒")
    val unitsMillis = arrayOf(86400000L, 3600000L, 60000L, 1000L)
    //31147787
    //3600000
    return when (millis) {
        in unitsMillis[3]..unitsMillis[2] -> "${millis / unitsMillis[3]} ${units[3]}"
        in unitsMillis[2]..unitsMillis[1] -> "${millis / unitsMillis[2]} ${units[2]}"
        in unitsMillis[1]..unitsMillis[0] -> "${millis / unitsMillis[1]} ${units[1]}"
        else -> "${millis / unitsMillis[0]}${units[0]}"
    }
}

