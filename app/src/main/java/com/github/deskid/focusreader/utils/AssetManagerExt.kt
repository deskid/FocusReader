package com.github.deskid.focusreader.utils

import android.content.res.AssetManager
import java.nio.charset.Charset

fun AssetManager.fileToString(subdirectory: String, filename: String): String {
    return open("$subdirectory/$filename").use {
        it.readBytes().toString(Charset.defaultCharset())
    }
}

fun AssetManager.fileToString(filename: String): String {
    return fileToString("", filename)
}