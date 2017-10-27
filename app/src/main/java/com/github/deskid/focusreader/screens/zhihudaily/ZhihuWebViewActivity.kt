package com.github.deskid.focusreader.screens.zhihudaily

import android.app.Activity
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.app.App
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.kotlinplay.ui.ToolbarManager
import com.r0adkll.slidr.Slidr
import kotlinx.android.synthetic.main.activity_zhihu_web_view.*
import org.jetbrains.anko.find
import javax.inject.Inject

class ZhihuWebViewActivity : AppCompatActivity(), ToolbarManager, LifecycleRegistryOwner {
    private var mLifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry {
        return mLifecycleRegistry
    }

    @Inject
    lateinit var factory: WebViewModel.WebViewModelFactory

    private val webViewModel: WebViewModel by lazyFast {
        ViewModelProviders.of(this, factory).get(WebViewModel::class.java)
    }

    override val toolbar by lazyFast { find<Toolbar>(R.id.toolbar) }

    companion object {
        fun start(activity: Activity, id: String, textView: TextView, imageView: ImageView) {
            val starter = Intent(activity, ZhihuWebViewActivity::class.java)
            starter.putExtra("ID", id)
//            val imagePair = Pair.create(imageView as View, imageView.transitionName)
//            val textPair = Pair.create(textView as View, textView.transitionName)
//            val options = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
//            activity.startActivity(starter, options)

            //transition with webView is too laggy，so I give up transition here
            activity.startActivity(starter)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhihu_web_view)

        (applicationContext as App).appComponent.inject(this)

        initToolbar()
        enableHomeAsUp { onBackPressed() }

        val id: String = intent.getStringExtra("ID")

        if (!TextUtils.isEmpty(id)) {
            toolbar_img.transitionName = id + "image"
            toolbar_title.transitionName = id + "title"
            webViewModel.getContent(id).observe(this, Observer {
                it?.let {
                    webview_container.post {
                        webview_container.loadDataWithBaseURL("file:///android_asset/", it.body, "text/html", "UTF-8", null)
                    }
                    if (!TextUtils.isEmpty(it.image)) {
                        toolbar_img.setImageUrl(it.image, {}, { _ ->
                            toolbar_img?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                                override fun onPreDraw(): Boolean {
                                    toolbar_img?.viewTreeObserver?.removeOnPreDrawListener(this)
                                    startPostponedEnterTransition()
                                    return true
                                }
                            })
                        })
                    }
                    toolbar_title.text = it.title
                }
            })
        }

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
