package com.github.deskid.focusreader.widget

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class ScrollableRecyclerView(context: Context?, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    var loadMoreListener: () -> Unit = {}

    var isLoading: Boolean = false

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (!isLoading && !ViewCompat.canScrollVertically(this, 1)) {
            loadMoreListener()
            isLoading = true
        } else {
            isLoading = false
        }
    }
}