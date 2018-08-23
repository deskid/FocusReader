package com.github.deskid.focusreader.screens.penti.yitu

import android.app.ActivityOptions
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.screens.ContentListFragment
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.refreshing

class ZenImageFragment : ContentListFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_zenimage_list

    var currentPage: Int = 1

    lateinit var adapter: ZenItemRecyclerViewAdapter

    private val viewModel: ZenImageViewModel by lazyFast {
        ViewModelProviders.of(this).get(ZenImageViewModel::class.java)
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        adapter = ZenItemRecyclerViewAdapter(ArrayList())
        adapter.setOnClickListener { position, images, holder ->
            // val imagePair = Pair.create(imageView as View, imageView.transitionName)
            val textPair = Pair.create(holder.mTitleView as View, holder.mTitleView.transitionName)
            val options = ActivityOptions.makeSceneTransitionAnimation(activity, textPair).toBundle()
            ZenImageDetailAct.start(activity!!, position, images, options)
        }
        view.adapter = adapter

        viewModel.refreshState.observe(this, Observer {
            when (it) {
                is UIState.LoadingState -> swiper.refreshing = true
                is UIState.LoadedState -> swiper.refreshing = false
                is UIState.ErrorState, is UIState.NetworkErrorState -> handleError(it)
            }
        })

        viewModel.getLiveData().observe(this, Observer { it?.let(adapter::addData) })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }

        val selectedItem = data.getIntExtra("SELECTED_POSITION", 0)
        view.scrollToPosition(selectedItem)
    }

    companion object {
        fun newInstance(): ZenImageFragment {
            return ZenImageFragment()
        }
    }

    override fun load() {
        viewModel.load()
    }

    override fun loadMore() {
        viewModel.load(++currentPage)
    }

    override fun getItemOffset(): Int = 0
}
