package com.github.deskid.focusreader.activity

import com.github.deskid.focusreader.utils.warmUpCustomTabs
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {
    override fun onStart() {
        super.onStart()
        warmUpCustomTabs()
    }
}