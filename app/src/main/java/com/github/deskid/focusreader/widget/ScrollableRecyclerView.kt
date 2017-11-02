package com.github.deskid.focusreader.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class ScrollableRecyclerView(context: Context?, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    var loadMoreListener: () -> Unit = {}

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (!canScrollVertically(1)) {
            loadMoreListener()
        }
    }
}