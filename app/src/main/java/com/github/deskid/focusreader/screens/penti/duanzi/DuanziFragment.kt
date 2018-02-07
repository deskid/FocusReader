package com.github.deskid.focusreader.screens.penti.duanzi

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.Duanzi
import com.github.deskid.focusreader.api.data.NetworkState
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.refreshing
import javax.inject.Inject

class DuanziFragment : ContentListFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_jokeitem_list
    }

    @Inject
    lateinit var factory: DuanziViewModel.JokeFactory

    private var currentPage: Int = 1

    private val viewModel: DuanziViewModel by lazyFast {
        ViewModelProviders.of(this, factory).get(DuanziViewModel::class.java)
    }

    private lateinit var adapter: DuanziItemRecyclerViewAdapter

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        adapter = DuanziItemRecyclerViewAdapter(emptyList<Duanzi>().toMutableList())
        view.adapter = adapter

        viewModel.refreshState.observe(this, Observer {
            when (it) {
                NetworkState.LOADING -> swiper.refreshing = true
                NetworkState.LOADED -> swiper.refreshing = false
                else -> Toast.makeText(context, it?.msg, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.duanziList.observe(this, Observer {
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
        viewModel.load(++currentPage + 1)
    }

}
