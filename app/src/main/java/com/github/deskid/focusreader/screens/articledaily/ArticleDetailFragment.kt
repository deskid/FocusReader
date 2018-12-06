package com.github.deskid.focusreader.screens.articledaily

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.UIState

import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.refreshing
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_article_detail.*

class ArticleDetailFragment : DaggerFragment() {

    private val viewModel: ArticleDetailViewModel by lazyFast {
        ViewModelProviders.of(this).get(ArticleDetailViewModel::class.java)
    }

    lateinit var type: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article_detail, container, false)
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        type = arguments?.getString("TYPE") ?: ""

        if (!TextUtils.isEmpty(type)) {
            load(type)
            swiper.setOnRefreshListener {
                load(type)
            }
        }

        viewModel.refreshState.observe(this, Observer {
            when (it) {
                is UIState.LoadingState -> swiper.refreshing = true
                is UIState.LoadedState -> swiper.refreshing = false
                is UIState.ErrorState -> {
                    swiper.refreshing = false
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                }
                is UIState.NetworkErrorState -> {
                    swiper.refreshing = false
                    Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.getData().observe(this, Observer {
            it?.let {
                author.text = it.data.author
                title.text = it.data.title
                @Suppress("DEPRECATION")
                content.text = Html.fromHtml(it.data.content)
            }
        })
    }

    fun load(type: String) {
        viewModel.load(type)
    }

    companion object {
        fun newInstance(type: String): ArticleDetailFragment {
            val fragment = ArticleDetailFragment()
            val bundle = Bundle()
            bundle.putString("TYPE", type)
            fragment.arguments = bundle
            return fragment
        }
    }
}