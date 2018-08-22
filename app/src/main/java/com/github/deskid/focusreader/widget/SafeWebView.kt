package com.github.deskid.focusreader.widget

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.util.AttributeSet
import android.webkit.*
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import com.github.deskid.focusreader.R

/**
 * 自带 progress 的 webview
 */
class SafeWebView : WebView {

    var progressBar: ProgressDialog

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {

        progressBar = ProgressDialog(context, R.style.ProgressbarStyle)
        progressBar.isIndeterminate = true
        progressBar.setIndeterminateDrawable(resources.getDrawable(R.drawable.progressbar))

        // 需要js代码实现写入storage，这里只是client端打开没用。
//        settings.domStorageEnabled = true //启用DOM缓存
        // 同上，javascript 都没开, 这里打开没用。
//        settings.databaseEnabled = true  //启用数据库缓存

        // App cache 需要 h5 web 在 Html 的 manifest 中申明，
        // 这里控制不了加载页的h5 代码，实际上下面代码无效
//        settings.setAppCacheEnabled(true)
//        settings.setAppCachePath(context.cacheDir.absolutePath)

//        if (context.isNetworkAvailable()) {
//            settings.cacheMode = WebSettings.LOAD_DEFAULT //优先加载没有过期的缓存，如果缓存不可用就从网络上加载数据
//        } else {
//            settings.cacheMode = WebSettings.LOAD_CACHE_ONLY //优先加载没有过期的缓存，如果缓存不可用就从网络上加载数据
//        }

        settings.useWideViewPort = true  //调整图片到wbview大小
        settings.loadWithOverviewMode = true //缩放至屏幕大小
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
        }

        setWebContentsDebuggingEnabled(true)

        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress == 100) {
                    progressBar.dismiss()
                }
            }
        }

        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.show()
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }

            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest?): WebResourceResponse? {
                return null
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar.dismiss()
            }
        }
    }
}
