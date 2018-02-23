package com.github.deskid.focusreader.activity

import android.os.Bundle
import android.os.Handler
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.widget.image.setImageUrl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import javax.inject.Inject

class SplashActivity : BaseActivity() {
    @Inject
    lateinit var splashViewModel: SplashViewModel

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        shimmer_view_container.startShimmerAnimation()
//        fullscreen_content.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LOW_PROFILE or
//                View.SYSTEM_UI_FLAG_FULLSCREEN or
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        val disposable = splashViewModel.splashImage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    web_imageview.setImageUrl(it, {
                        val textSwatch = it?.lightVibrantSwatch
                        textSwatch?.let { swatch ->
                            fullscreen_content.setTextColor(swatch.bodyTextColor)
                        }
                    }, {
                        web_imageview.postDelayed({
                            jumpMainAct()
                        }, 2000)
                    })
                }, {
                    toast(it.message ?: "something wrong")
                    jumpMainAct()
                })

        mCompositeDisposable.add(disposable)
    }

    private fun jumpMainAct() {
        startActivity<MainActivity>()
        overridePendingTransition(0, 0)
        mHandler.postDelayed({ finish() }, 2000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        jumpMainAct()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }
}
