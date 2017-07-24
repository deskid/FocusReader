package com.github.deskid.focusreader.screens.yitu

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.utils.refreshing
import com.github.deskid.focusreader.widget.ScrollableRecyclerView
import javax.inject.Inject

class ZenImageFragment : LifecycleFragment() {

    lateinit var view: ScrollableRecyclerView
    lateinit var swiper: SwipeRefreshLayout

    var currentPage: Int = 1

    lateinit var adapter: ZenItemRecyclerViewAdapter

    @Inject
    lateinit var factory: ZenImageViewModel.ZenImageFactory

    val viewModel: ZenImageViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ZenImageViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        swiper = inflater.inflate(R.layout.fragment_zenimage_list, container, false) as SwipeRefreshLayout
        view = swiper.findViewById(R.id.list) as ScrollableRecyclerView
        view.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        view.loadMoreListener = { loadMore() }
        swiper.setOnRefreshListener { load() }

        return swiper
    }

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        adapter = ZenItemRecyclerViewAdapter(ArrayList())
        view.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        load()
    }

    companion object {
        fun newInstance(): ZenImageFragment {
            return ZenImageFragment()
        }
    }

    fun load(page: Int = 1) {
        swiper.refreshing = true
        viewModel.load(page, true).observe(this, Observer {
            swiper.refreshing = false
            currentPage = 1
            it?.let {
                if (it.size < 30) {
                    adapter.addData(it, true)
                } else {
                    adapter.swipeData(it)
                }
            }
        })
    }

    fun loadMore() {
        viewModel.load(currentPage + 1).observe(this, Observer {
            if (it != null && !(it.isEmpty())) {
                currentPage++
                adapter.addData(it)
            } else {
                Snackbar.make(swiper, "No more data", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}
