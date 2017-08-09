package com.github.deskid.focusreader.utils

import com.github.deskid.focusreader.db.AppDatabase

fun AppDatabase.transaction(action: () -> Unit): Unit {
    beginTransaction()
    try {
        action()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}