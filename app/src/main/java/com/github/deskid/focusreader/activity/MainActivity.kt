package com.github.deskid.focusreader.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.screens.PentiFragment
import com.github.deskid.focusreader.screens.duanzi.DuanziFragment
import com.github.deskid.focusreader.screens.yitu.ZenImageFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val screens: SparseArray<Fragment> = SparseArray(3)

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.navigation_home -> {
                if (screens[0] == null) {
                    screens.put(0, ZenImageFragment.newInstance())
                }
                fragment = screens[0]
            }
            R.id.navigation_dashboard -> {
                if (screens[1] == null) {
                    screens.put(1, PentiFragment.newInstance())
                }
                fragment = screens[1]
            }
            R.id.navigation_notifications -> {
                if (screens[2] == null) {
                    screens.put(2, DuanziFragment.newInstance())
                }
                fragment = screens[2]
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.selectedItemId = R.id.navigation_dashboard
    }
}
