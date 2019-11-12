package com.github.deskid.focusreader.activity

import android.os.Bundle
import android.os.Handler
import com.github.deskid.focusreader.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
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
        jumpMainAct()
    }

    private fun jumpMainAct() {
        mHandler.postDelayed({
            startActivity<MainActivity>()
            overridePendingTransition(0, 0)
            mHandler.postDelayed({ finish() }, 2000)
        }, 200)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }
}
