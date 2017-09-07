package com.github.deskid.focusreader.widget

/**
 * Log.d(Tag,"balabala")
 */
val Any.Tag: String
    get() = this::class.java.simpleName