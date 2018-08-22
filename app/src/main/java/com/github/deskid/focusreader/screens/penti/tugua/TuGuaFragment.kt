package com.github.deskid.focusreader.screens.penti.tugua

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.api.data.UIState.*
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.refreshing

class TuGuaFragment : ContentListFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_tuguaitem_list
    }

    var currentPage: Int = 1

    private val viewModel: TuGuaViewModel by lazyFast {
        ViewModelProviders.of(this).get(TuGuaViewModel::class.java)
    }

    lateinit var adapter: TuGuaItemRecyclerViewAdapter

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        adapter = TuGuaItemRecyclerViewAdapter(emptyList<TuGua>().toMutableList())
        view.adapter = adapter

        viewModel.refreshState.observe(this, Observer {
            when (it) {
                is LoadingState -> swiper.refreshing = true
                is LoadedState -> swiper.refreshing = false
                is ErrorState -> handleError(it)
            }
        })
        viewModel.getLiveData().observe(this, Observer {
            it?.let {
                adapter.addData(it)
            }
        })

    }

    companion object {
        fun newInstance(): TuGuaFragment {
            return TuGuaFragment()
        }
    }

    override fun load() {
        viewModel.load()
    }

    override fun loadMore() {
        viewModel.load(++currentPage)
    }
}
