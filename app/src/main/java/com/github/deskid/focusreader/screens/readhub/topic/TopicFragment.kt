package com.github.deskid.focusreader.screens.readhub.topic

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.NetworkState
import com.github.deskid.focusreader.api.data.Topic
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.refreshing
import javax.inject.Inject

class TopicFragment : ContentListFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_readhub_topic_list
    }

    @Inject
    lateinit var factory: TopicViewModel.Factory

    private var lastCursor: Long? = null

    private val viewModel: TopicViewModel by lazyFast {
        ViewModelProviders.of(this, factory).get(TopicViewModel::class.java)
    }

    private lateinit var adapter: TopicAdapter

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        adapter = TopicAdapter(emptyList<Topic>().toMutableList())
        view.adapter = adapter


        viewModel.refreshState.observe(this, Observer {
            when (it) {
                NetworkState.LOADING -> swiper.refreshing = true
                NetworkState.LOADED -> swiper.refreshing = false
                else -> Toast.makeText(context, it?.msg, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.topics.observe(this, Observer {
            it?.let {
                adapter.addData(it.data)
                lastCursor = when {
                    it.data.isNotEmpty() -> it.data.last().order
                    else -> null
                }
            }
        })

    }

    companion object {
        fun newInstance(): TopicFragment {
            return TopicFragment()
        }
    }

    override fun load() {
        viewModel.load()
    }

    override fun loadMore() {
        viewModel.loadMore(lastCursor)
    }
}