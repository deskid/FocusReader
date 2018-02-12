package com.github.deskid.focusreader.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class ScrollableRecyclerView(context: Context?, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    var loadMoreListener: () -> Unit = {}
//
//    override fun onScrollStateChanged(state: Int) {
//        super.onScrollStateChanged(state)
//        if (!canScrollVertically(1)) {
//            loadMoreListener()
//        }
//    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        if (dy > 0 && !canScrollVertically(1)) {
            if (layoutManager is LinearLayoutManager && (layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == adapter.itemCount - 1) {
                loadMoreListener()
            }
        }
    }
}