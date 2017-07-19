package com.github.deskid.focusreader.widget

import android.content.Context
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.util.AttributeSet
import android.webkit.*
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

class SafeWebView : WebView {

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {

        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setAppCacheEnabled(true)
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        settings.useWideViewPort = true  //调整图片到wbview大小
        settings.loadWithOverviewMode = true //缩放至屏幕大小
        settings.loadsImagesAutomatically = true //支持自动加载图片
        settings.blockNetworkImage = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
        }

        setWebViewClient(object : WebViewClient() {
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
                view.settings.blockNetworkImage = false
            }
        })
    }

    private val AD_KEYWORDS = arrayOf("google", "show_ads", "ad", "taobao", "alibaba", "tmall", "tianmao", "jd", "jingdong", "mougujie", "weidian", "360buy", "baidu")
}
