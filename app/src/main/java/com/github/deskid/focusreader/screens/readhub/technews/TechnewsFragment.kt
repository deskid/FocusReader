package com.github.deskid.focusreader.screens.readhub.technews

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.SimpleTopic
import com.github.deskid.focusreader.app.App
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as App).appComponent.inject(this)
    }

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        adapter = TechnewsAdapter(emptyList<SimpleTopic>().toMutableList())
        view.adapter = adapter
    }

    companion object {
        fun newInstance(): TechnewsFragment {
            return TechnewsFragment()
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
                lastCursor = it.data[count - 1].publishDate.withoutSuffix().toDate().time
            } else {
                lastCursor = -1
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
                lastCursor = it.data[count - 1].publishDate.withoutSuffix().toDate().time
                adapter.addData(it.data)
            }
        })
    }
}