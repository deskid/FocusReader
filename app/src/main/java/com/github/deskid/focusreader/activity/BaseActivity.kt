package com.github.deskid.focusreader.activity

import android.support.v7.app.AppCompatActivity
import com.github.deskid.focusreader.utils.warmUpCustomTabs

abstract class BaseActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        warmUpCustomTabs()
    }
}