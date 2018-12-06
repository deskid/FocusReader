package com.github.deskid.focusreader.screens.penti.duanzi

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.Duanzi
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.utils.switchMap
import com.github.deskid.focusreader.widget.refreshing

class DuanziFragment : ContentListFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_jokeitem_list
    }

    private var currentPage: Int = 1

    private val viewModel: DuanziViewModel by lazyFast {
        ViewModelProviders.of(this).get(DuanziViewModel::class.java)
    }

    private lateinit var adapter: DuanziItemRecyclerViewAdapter

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        adapter = DuanziItemRecyclerViewAdapter(emptyList<Duanzi>().toMutableList())
        view.adapter = adapter

        viewModel.refreshState.observe(this, Observer {
            when (it) {
                is UIState.LoadingState -> swiper.refreshing = true
                is UIState.LoadedState -> swiper.refreshing = false
                is UIState.ErrorState, is UIState.NetworkErrorState -> handleError(it)
            }
        })

        viewModel.getCurrentPage().switchMap {
            return@switchMap viewModel.getData(it)
        }.observe(this, Observer {
            it?.let {
                adapter.addData(it)
            }
        })
    }

    companion object {
        fun newInstance(): DuanziFragment {
            return DuanziFragment()
        }
    }

    override fun load() {
        viewModel.load()
    }

    override fun loadMore() {
        viewModel.load(++currentPage)
    }

}
