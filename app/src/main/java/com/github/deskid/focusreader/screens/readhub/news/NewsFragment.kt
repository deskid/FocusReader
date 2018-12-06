package com.github.deskid.focusreader.screens.readhub.news

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.SimpleTopic
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.utils.sub
import com.github.deskid.focusreader.utils.toDate
import com.github.deskid.focusreader.widget.refreshing

class NewsFragment : ContentListFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_readhub_topic_list
    }

    private var lastCursor: Long = 1

    private val viewModel: NewsViewModel by lazyFast {
        ViewModelProviders.of(this).get(NewsViewModel::class.java)
    }

    private lateinit var adapter: NewsAdapter

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        adapter = NewsAdapter(emptyList<SimpleTopic>().toMutableList())
        view.adapter = adapter
        viewModel.refreshState.observe(this, Observer {
            when (it) {
                is UIState.LoadingState -> swiper.refreshing = true
                is UIState.LoadedState -> swiper.refreshing = false
                is UIState.ErrorState, is UIState.NetworkErrorState -> handleError(it)
            }
        })

        viewModel.getData().observe(this, Observer {
            it?.let {
                adapter.addData(it.data)
                lastCursor = when {
                    it.data.isNotEmpty() -> it.data.last().publishDate.sub().toDate().time
                    else -> -1
                }
            }
        })
    }

    companion object {
        fun newInstance(): NewsFragment {
            return NewsFragment()
        }
    }

    override fun load() {
        viewModel.load()
    }

    override fun loadMore() {
        viewModel.load(lastCursor)
    }
}