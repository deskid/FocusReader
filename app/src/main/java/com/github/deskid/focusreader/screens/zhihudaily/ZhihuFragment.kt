package com.github.deskid.focusreader.screens.zhihudaily

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.refreshing

class ZhihuFragment : ContentListFragment() {

    private lateinit var adapter: ZhihuAdapter

    private lateinit var date: String

    private val viewModel: ZhihuViewModel by lazyFast {
        ViewModelProviders.of(this).get(ZhihuViewModel::class.java)
    }

    override fun getLayoutId(): Int = R.layout.fragment_zhihu_list

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        adapter = ZhihuAdapter(activity!!, ArrayList())
        view.adapter = adapter

        viewModel.refreshState.observe(this, Observer {
            when (it) {
                is UIState.LoadingState -> swiper.refreshing = true
                is UIState.LoadedState -> swiper.refreshing = false
                is UIState.ErrorState, is UIState.NetworkErrorState -> handleError(it)
            }
        })

        viewModel.getLiveData().observe(this, Observer {
            it?.let {
                adapter.addData(it.stories)
                date = it.date
            }
        })
    }

    override fun load() {
        viewModel.load()
    }

    companion object {
        fun newInstance(): ZhihuFragment {
            return ZhihuFragment()
        }
    }

    override fun loadMore() {
        viewModel.loadMore(date)
    }
}