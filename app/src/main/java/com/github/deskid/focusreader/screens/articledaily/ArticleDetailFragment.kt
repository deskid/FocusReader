package com.github.deskid.focusreader.screens.articledaily

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.refreshing
import kotlinx.android.synthetic.main.fragment_article_detail.*
import javax.inject.Inject

class ArticleDetailFragment : LifecycleFragment() {
    @Inject
    lateinit var factory: ArticleDetailViewModel.Factory

    private val viewModel: ArticleDetailViewModel by lazyFast {
        ViewModelProviders.of(this, factory).get(ArticleDetailViewModel::class.java)
    }

    lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article_detail, container, false)
    }

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        type = arguments.getString("TYPE")

        if (!TextUtils.isEmpty(type)) {
            load(type)
        }

        swiper.setOnRefreshListener {
            load(type)
        }
    }

    fun load(type: String) {
        swiper.refreshing = true
        viewModel.load(type).observe(this, Observer {
            swiper.refreshing = false
            it?.let {
                author.text = it.data.author
                title.text = it.data.title
                content.text = Html.fromHtml(it.data.content)
            }

        })
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