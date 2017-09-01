package com.github.deskid.focusreader.activity

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.app.App
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity(), LifecycleRegistryOwner {

    @Inject
    lateinit var factory: SplashViewModel.Factory

    private val lifecycleRegistry = LifecycleRegistry(this)

    private val splashViewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as App).appComponent.inject(this)

        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        splashViewModel.splashImage.observe(this, Observer {
            web_imageview.setImageUrl(it?.data)
            web_imageview.postDelayed({
                startActivity<MainActivity>()
                finish()
            }, 200)
        })
    }

}
