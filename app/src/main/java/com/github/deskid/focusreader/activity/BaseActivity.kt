package com.github.deskid.focusreader.activity

import android.widget.Toast
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.UIState
import com.github.deskid.focusreader.utils.warmUpCustomTabs
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {
    override fun onStart() {
        super.onStart()
        warmUpCustomTabs()
    }

    protected fun handleError(error: UIState) {
        when (error) {
            is UIState.ErrorState -> Toast.makeText(this, error.msg, Toast.LENGTH_SHORT).show()
            is UIState.NetworkErrorState -> Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
        }
    }
}