package com.github.deskid.focusreader.screens.duanzi

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.Duanzi
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.utils.refreshing
import javax.inject.Inject

class DuanziFragment : ContentListFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_jokeitem_list
    }

    @Inject
    lateinit var factory: DuanziViewModel.JokeFactory

    var currentPage: Int = 1

    val viewModel: DuanziViewModel by lazy {
        ViewModelProviders.of(this, factory).get(DuanziViewModel::class.java)
    }

    lateinit var adapter: DuanziItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as App).appComponent.inject(this)
    }

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        adapter = DuanziItemRecyclerViewAdapter(emptyList<Duanzi>().toMutableList())
        view.adapter = adapter
    }

    companion object {
        fun newInstance(): DuanziFragment {
            return DuanziFragment()
        }
    }

    override fun load(page: Int) {
        swiper.refreshing = true
        viewModel.load(page).observe(this, Observer {
            swiper.refreshing = false
            adapter.swipeData(it?.data ?: emptyList())
        })
    }

    override fun loadMore() {
        swiper.refreshing = true
        viewModel.load(currentPage + 1).observe(this, Observer {
            swiper.refreshing = false
            if (it != null && it.data != null) {
                currentPage++
                adapter.addData(it.data ?: emptyList())
            }
        })
    }

}
