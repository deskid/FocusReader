package com.github.deskid.focusreader.widget

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.util.AttributeSet
import android.webkit.*
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import com.github.deskid.focusreader.utils.isNetworkAvailable

class SafeWebView : WebView {

    var progressBar: ProgressDialog

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {

        progressBar = ProgressDialog(context)

        settings.domStorageEnabled = true //启用DOM缓存
        settings.databaseEnabled = true  //启用数据库缓存
        settings.setAppCacheEnabled(true)
        settings.setAppCachePath(context.cacheDir.absolutePath)

        if (context.isNetworkAvailable()) {
            settings.cacheMode = WebSettings.LOAD_DEFAULT //优先加载没有过期的缓存，如果缓存不可用就从网络上加载数据
        } else {
            settings.cacheMode = WebSettings.LOAD_CACHE_ONLY //优先加载没有过期的缓存，如果缓存不可用就从网络上加载数据
        }


        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        settings.useWideViewPort = true  //调整图片到wbview大小
        settings.loadWithOverviewMode = true //缩放至屏幕大小
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
        }

        setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
            }
        })

        setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.show()
            }

            override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                val response = WebResourceResponse("text/plain", "UTF-8", null)
                val uri = Uri.parse(url)
                val host = uri.host

                //主站资源不拦截
                if (url.contains("dapenti") || url.contains("penti")) {
                    return null
                }

                if ((url.endsWith(".img") || url.endsWith(".jpg") || url.endsWith(".png"))) {
                    return null
                }

                //拦截百度域名
                if (host == "pos.baidu.com" || host == "eclick.baidu.com") {
                    return response
                }

                AD_KEYWORDS.forEach {
                    if (url.contains(it)) {
                        return response
                    }
                }

                // 过滤js请求
                if ((url.endsWith(".js") || url.contains("javascript") || url.contains("script"))) {
                    return response
                }

                // 过滤百度统计相关请求
                if (url.contains("pos.baidu.com") || url.contains("baidustatic")) {
                    return response
                }

                return null
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar.dismiss()
            }
        })
    }

    private val AD_KEYWORDS = arrayOf("google", "show_ads", "ad", "taobao", "alibaba", "tmall", "tianmao", "jd", "jingdong", "mougujie", "weidian", "360buy", "baidu")
}
