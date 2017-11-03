package com.github.deskid.focusreader.screens.readhub.topic

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.Topic
import com.github.deskid.focusreader.app.App
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as App).appComponent.inject(this)
    }

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        adapter = TopicAdapter(emptyList<Topic>().toMutableList())
        view.adapter = adapter
    }

    companion object {
        fun newInstance(): TopicFragment {
            return TopicFragment()
        }
    }

    override fun load(onLoaded: () -> Unit) {
        swiper.refreshing = true
        viewModel.load().observe(this, Observer {
            swiper.refreshing = false
            onLoaded()
            if (it?.data != null && it.data.size > 0) {
                adapter.swipeData(it.data)
                val count = it.data.size
                lastCursor = it.data[count - 1].order
            }

        })
    }

    override fun loadMore(onLoaded: () -> Unit) {
        swiper.refreshing = true
        viewModel.loadMore(lastCursor).observe(this, Observer {
            swiper.refreshing = false
            onLoaded()
            if (it?.data != null && it.data.size > 0) {
                adapter.addData(it.data)
                val count = it.data.size
                lastCursor = it.data[count - 1].order
                adapter.addData(it.data)
            }
        })
    }
}