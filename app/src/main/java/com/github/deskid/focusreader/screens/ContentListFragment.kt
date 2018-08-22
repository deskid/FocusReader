package com.github.deskid.focusreader.screens

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.utils.dp2Px
import com.github.deskid.focusreader.utils.getColorCompat
import com.github.deskid.focusreader.widget.ScrollableRecyclerView
import com.github.deskid.focusreader.widget.refreshing
import dagger.android.support.DaggerFragment

abstract class ContentListFragment : DaggerFragment() {

    abstract fun getLayoutId(): Int

    private var mContentView: View? = null
    private var mIsNeedInitData: Boolean = true
    protected lateinit var view: ScrollableRecyclerView
    protected lateinit var swiper: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (mContentView != null) {
            mIsNeedInitData = false
        }

        mContentView = inflater.inflate(getLayoutId(), container, false)
        swiper = mContentView!!.findViewById(R.id.swiper)
        swiper.setColorSchemeColors(context.getColorCompat(R.color.colorPrimaryLight))
        view = swiper.findViewById(R.id.list)
        view.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect?.set(0, 0, 0, getItemOffset())
            }
        })

        view.layoutManager = LinearLayoutManager(context)

        view.loadMoreListener = { loadMore() }
        swiper.setOnRefreshListener { load() }

        return mContentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mIsNeedInitData) {
            load()
        }
    }

    abstract fun load()

    abstract fun loadMore()

    protected open fun getItemOffset(): Int = context.dp2Px(8)

    protected open fun handleError(error: UIState.ErrorState) {
        swiper.refreshing = false
        Toast.makeText(context, error.msg, Toast.LENGTH_SHORT).show()
    }
}