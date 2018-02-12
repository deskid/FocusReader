package com.github.deskid.focusreader.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.utils.getColorCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    val screens: SparseArray<Fragment> = SparseArray(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.offscreenPageLimit = 4
        val adapter = MainPagerAdapter(supportFragmentManager)
        view_pager.adapter = adapter

        bottom_navigation.setOnTabSelectedListener { position, wasSelected ->
            if (!wasSelected) {
                view_pager.currentItem = position
            }
            return@setOnTabSelectedListener true
        }

        val navigationAdapter = AHBottomNavigationAdapter(this, R.menu.navigation)
        navigationAdapter.setupWithBottomNavigation(bottom_navigation)
        bottom_navigation.isBehaviorTranslationEnabled = true
        bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
        bottom_navigation.accentColor = getColorCompat(R.color.colorPrimary)
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }
}
