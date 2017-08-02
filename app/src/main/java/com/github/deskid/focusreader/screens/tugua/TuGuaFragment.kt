package com.github.deskid.focusreader.screens.tugua

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.widget.refreshing
import javax.inject.Inject

class TuGuaFragment : ContentListFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_tuguaitem_list
    }

    var currentPage: Int = 1

    @Inject
    lateinit var factory: TuGuaViewModel.TuGuaFactory

    val viewModel: TuGuaViewModel by lazy {
        ViewModelProviders.of(this, factory).get(TuGuaViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as App).appComponent.inject(this)
    }

    lateinit var adapter: TuGuaItemRecyclerViewAdapter

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        adapter = TuGuaItemRecyclerViewAdapter(emptyList<TuGua>().toMutableList())
        view.adapter = adapter
    }

    companion object {
        fun newInstance(): TuGuaFragment {
            return TuGuaFragment()
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
