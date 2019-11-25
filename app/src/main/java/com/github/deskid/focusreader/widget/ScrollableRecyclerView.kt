package com.github.deskid.focusreader.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet


class ScrollableRecyclerView(context: Context, attrs: AttributeSet?) :
    RecyclerView(context, attrs) {

    var loadMoreListener: () -> Unit = {}

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        val lm = layoutManager as LinearLayoutManager
        val totalItemCount = adapter?.itemCount
        val lastVisibleItemPosition = lm.findLastVisibleItemPosition()
        val visibleItemCount = childCount

        if (totalItemCount != null) {
            if (state == SCROLL_STATE_IDLE
                    && lastVisibleItemPosition == totalItemCount - 1
                    && visibleItemCount > 0) {
                loadMoreListener()
            }
        }

    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        if (dy > 0 && !canScrollVertically(1)) {
            if (layoutManager is LinearLayoutManager && (layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == (adapter?.itemCount ?: 0) - 1) {
                loadMoreListener()
            }
        }
    }
}