package com.github.deskid.focusreader.screens.readhub

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.activity.BaseActivity
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.utils.launchUrlWithCustomTabs
import com.github.deskid.focusreader.utils.lazyFast
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_zhihu_web_view.*

class InstantViewActivity : BaseActivity() {

    private val viewModel: InstantViewModel by lazyFast {
        ViewModelProviders.of(this).get(InstantViewModel::class.java)
    }

    companion object {
        fun start(context: Context, topicId: String) {
            val starter = Intent(context, InstantViewActivity::class.java)
            starter.putExtra("topicId", topicId)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instant_view)

        val id: String? = intent.getStringExtra("topicId")

        viewModel.refreshState.observe(this, Observer {
            when (it) {
                is UIState.ErrorState, is UIState.NetworkErrorState -> handleError(it)
            }
        })

        if (!TextUtils.isEmpty(id)) {
            viewModel.getContent(id!!)
            viewModel.getData().observe(this, Observer {
                it?.let {
                    webview_container.loadDataWithBaseURL("file:///android_asset/", it.content, "text/html", "UTF-8", null)
                    toolbar_title.text = it.title
                }
            })
        } else {
            val cid = intent.data.getQueryParameter("topicId")
            val instantview = intent.data.getQueryParameter("instantView")
            val mobileUrl = intent.data.getQueryParameter("mobileUrl")

            if (instantview == "true") {
                viewModel.getContent(cid)
                viewModel.getData().observe(this, Observer {
                    it?.let {
                        webview_container.loadDataWithBaseURL("file:///android_asset/", it.content, "text/html", "UTF-8", null)
                        toolbar_title.text = it.title
                    }
                })
            } else {
                launchUrlWithCustomTabs(mobileUrl)
                finish()
            }
        }


        webview_container.isVerticalScrollBarEnabled = false
        webview_container.isHorizontalScrollBarEnabled = false
        Slidr.attach(this)
    }
}
