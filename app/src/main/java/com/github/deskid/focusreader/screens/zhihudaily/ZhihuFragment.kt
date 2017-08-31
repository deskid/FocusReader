package com.github.deskid.focusreader.screens.zhihudaily

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.widget.refreshing
import javax.inject.Inject

class ZhihuFragment : ContentListFragment() {

    private lateinit var adapter: ZhihuAdapter

    private lateinit var date: String

    @Inject
    lateinit var factory: ZhihuViewModel.ZhihuFactory

    private val viewModel: ZhihuViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ZhihuViewModel::class.java)
    }

    override fun getLayoutId(): Int = R.layout.fragment_zhihu_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as App).appComponent.inject(this)
    }

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        adapter = ZhihuAdapter(activity, ArrayList())
        view.adapter = adapter
    }

    override fun load(page: Int) {
        swiper.refreshing = true
        viewModel.load().observe(this, Observer {
            swiper.refreshing = false
            it?.let {
                adapter.swipeData(it.stories)
                date = it.date
            }
        })
    }

    companion object {
        fun newInstance(): ZhihuFragment {
            return ZhihuFragment()
        }
    }

    override fun loadMore() {
        viewModel.loadMore(date).observe(this, Observer {
            it?.let {
                adapter.addData(it.stories)
                date = it.date
            }
        })
    }
}