package com.github.deskid.focusreader.screens.readhub.technews

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.NetworkState
import com.github.deskid.focusreader.api.data.SimpleTopic
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.utils.toDate
import com.github.deskid.focusreader.utils.withoutSuffix
import com.github.deskid.focusreader.widget.refreshing
import javax.inject.Inject

class TechnewsFragment : ContentListFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_readhub_topic_list
    }

    @Inject
    lateinit var factory: TechnewsViewModel.Factory

    private var lastCursor: Long = 1

    private val viewModel: TechnewsViewModel by lazyFast {
        ViewModelProviders.of(this, factory).get(TechnewsViewModel::class.java)
    }

    private lateinit var adapter: TechnewsAdapter

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        adapter = TechnewsAdapter(emptyList<SimpleTopic>().toMutableList())
        view.adapter = adapter

        viewModel.refreshState.observe(this, Observer {
            when (it) {
                NetworkState.LOADING -> swiper.refreshing = true
                NetworkState.LOADED -> swiper.refreshing = false
                else -> Toast.makeText(context, it?.msg, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.technews.observe(this, Observer {
            it?.let {
                adapter.addData(it.data)
                lastCursor = when {
                    it.data.isNotEmpty() -> it.data.last().publishDate.withoutSuffix().toDate().time
                    else -> -1
                }
            }
        })
    }

    companion object {
        fun newInstance(): TechnewsFragment {
            return TechnewsFragment()
        }
    }

    override fun load() {
        viewModel.load()
    }

    override fun loadMore() {
        viewModel.loadMore(lastCursor)
    }
}