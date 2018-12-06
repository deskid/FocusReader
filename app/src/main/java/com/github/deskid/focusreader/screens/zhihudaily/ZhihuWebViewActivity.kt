package com.github.deskid.focusreader.screens.zhihudaily

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.ViewGroup
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.activity.BaseActivity
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.ToolbarManager
import com.github.deskid.focusreader.widget.image.setImageUrl
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_zhihu_web_view.*
import org.jetbrains.anko.find

class ZhihuWebViewActivity : BaseActivity(), ToolbarManager {

    private val webViewModel: WebViewModel by lazyFast {
        ViewModelProviders.of(this).get(WebViewModel::class.java)
    }

    override val toolbar by lazyFast { find<Toolbar>(R.id.toolbar) }

    companion object {
        fun start(activity: Activity, id: String) {
            val starter = Intent(activity, ZhihuWebViewActivity::class.java)
            starter.putExtra("ID", id)

            //transition with webView is too laggy，so I give up transition here
            activity.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhihu_web_view)

        initToolbar()
        enableHomeAsUp { onBackPressed() }

        val id: String = intent.getStringExtra("ID")

        if (!TextUtils.isEmpty(id)) {
            webViewModel.getContent(id)
        }

        webViewModel.refreshState.observe(this, Observer {
            when (it) {
                is UIState.ErrorState, is UIState.NetworkErrorState -> handleError(it)
            }
        })

        webViewModel.getData().observe(this, Observer {
            it?.let {
                webview_container.loadDataWithBaseURL("file:///android_asset/", it.body, "text/html", "UTF-8", null)
                toolbar_img.setImageUrl(it.image)
                toolbar_title.text = it.title
            }
        })

        webview_container.isVerticalScrollBarEnabled = false
        webview_container.isHorizontalScrollBarEnabled = false
        Slidr.attach(this)
    }

    override fun onPause() {
        super.onPause()
        webview_container.onPause()
    }

    override fun onResume() {
        super.onResume()
        webview_container.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        webview_container.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webview_container.clearHistory()
        (webview_container.parent as ViewGroup).removeView(webview_container)
        webview_container.destroy()

    }
}
