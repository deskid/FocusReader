package com.github.deskid.focusreader.screens.penti.tugua

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.Toast
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.activity.BaseActivity
import com.github.deskid.focusreader.api.data.ErrorState
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.ToolbarManager
import com.github.deskid.focusreader.widget.image.setImageUrl
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_web_view.*
import org.jetbrains.anko.find

class WebViewActivity : BaseActivity(), ToolbarManager {

    private val webViewModel: TuGuaWebViewModel by lazyFast {
        ViewModelProviders.of(this).get(TuGuaWebViewModel::class.java)
    }

    override val toolbar by lazyFast { find<Toolbar>(R.id.toolbar) }

    companion object {
        fun start(context: Context, url: String) {
            val starter = Intent(context, WebViewActivity::class.java)
            starter.putExtra("pageUrl", url)
            context.startActivity(starter)
        }

        fun start(context: Context, url: String, img: String) {
            val starter = Intent(context, WebViewActivity::class.java)
            starter.putExtra("pageUrl", url)
            starter.putExtra("pageImage", img)
            context.startActivity(starter)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        initToolbar()
        enableHomeAsUp { onBackPressed() }

        val pageUrl: String = intent.getStringExtra("pageUrl")

        val pageImage = intent.getStringExtra("pageImage")

        if (!TextUtils.isEmpty(pageUrl)) {
            webViewModel.getContent(pageUrl)
        }
        if (!TextUtils.isEmpty(pageImage)) {
            toolbar_img.setImageUrl(pageImage)
        }

        webViewModel.refreshState.observe(this, Observer {
            when (it) {
                is ErrorState -> Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
            }
        })

        webViewModel.data.observe(this, Observer {
            webview_container.loadDataWithBaseURL("file:///android_asset/", it, "text/html", "UTF-8", null)
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
