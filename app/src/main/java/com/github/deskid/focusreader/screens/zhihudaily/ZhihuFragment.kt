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

    lateinit var adapter: ZhihuAdapter

    @Inject
    lateinit var factory: ZhihuViewModel.ZhihuFactory

    val viewModel: ZhihuViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ZhihuViewModel::class.java)
    }

    override fun getLayoutId(): Int = R.layout.fragment_zhihu_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as App).appComponent.inject(this)
    }

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        adapter = ZhihuAdapter(ArrayList())
        view.adapter = adapter
    }

    override fun load(page: Int) {
        swiper.refreshing = true
        viewModel.load().observe(this, Observer {
            swiper.refreshing = false
            it?.let {
                adapter.swipeData(it.stories)
            }
        })
    }

    companion object {
        fun newInstance(): ZhihuFragment {
            return ZhihuFragment()
        }
    }

    override fun loadMore() {
        //do nothing
    }
}