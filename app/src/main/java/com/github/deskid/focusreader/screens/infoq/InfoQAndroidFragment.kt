package com.github.deskid.focusreader.screens.infoq

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.refreshing

class InfoQAndroidFragment : ContentListFragment() {

    private lateinit var adapter: InfoQAdapter

    private val viewModel: InfoQViewModel by lazyFast {
        ViewModelProviders.of(this).get(InfoQViewModel::class.java)
    }

    lateinit var type: String
    override fun getLayoutId(): Int {
        return R.layout.fragment_info_list
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        adapter = InfoQAdapter(activity!!)
        view.adapter = adapter

        viewModel.refreshState.observe(this, Observer {
            when (it) {
                is UIState.LoadingState -> swiper.refreshing = true
                is UIState.LoadedState -> swiper.refreshing = false
                is UIState.ErrorState, is UIState.NetworkErrorState -> handleError(it)
            }
        })

        viewModel.getData().observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    override fun load() {
        viewModel.load()
    }

    override fun loadMore() {
    }

    companion object {
        fun newInstance(): InfoQAndroidFragment {
            return InfoQAndroidFragment()
        }
    }
}
