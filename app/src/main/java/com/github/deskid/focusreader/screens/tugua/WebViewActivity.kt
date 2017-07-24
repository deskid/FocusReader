package com.github.deskid.focusreader.screens.tugua

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.ViewGroup
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.app.App
import com.github.deskid.kotlinplay.ui.ToolbarManager
import kotlinx.android.synthetic.main.activity_web_view.*
import org.jetbrains.anko.find
import javax.inject.Inject

class WebViewActivity : AppCompatActivity(), ToolbarManager, LifecycleRegistryOwner {
    internal var mLifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry {
        return mLifecycleRegistry
    }

    @Inject
    lateinit var factory: WebViewModel.WebViewModelFactory

    val webViewModel: WebViewModel by lazy {
        ViewModelProviders.of(this, factory).get(WebViewModel::class.java)
    }

    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

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

        (applicationContext as App).appComponent.inject(this)

        initToolbar()
        enableHomeAsUp { onBackPressed() }

        val pageUrl: String = intent.getStringExtra("pageUrl")

        val pageImage = intent.getStringExtra("pageImage")

        if (!TextUtils.isEmpty(pageUrl)) {
            webViewModel.getContent(pageUrl).observe(this, Observer {
                webview_container.loadDataWithBaseURL("file:///android_asset/", it, "text/html", "UTF-8", null)
            })
        }
        if (!TextUtils.isEmpty(pageImage)) {
            toolbar_img.setImageUrl(pageImage)
        }

        webview_container.isVerticalScrollBarEnabled = false
        webview_container.isHorizontalScrollBarEnabled = false

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
