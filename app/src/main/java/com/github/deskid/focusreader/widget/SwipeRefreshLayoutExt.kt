package com.github.deskid.focusreader.widget

import android.support.v4.widget.SwipeRefreshLayout

var SwipeRefreshLayout.refreshing: Boolean
    get() = isRefreshing()
    set(value) {
        when {
            value -> post { setRefreshing(value) }
            else -> postDelayed({ setRefreshing(value) }, 400)
        }
    }