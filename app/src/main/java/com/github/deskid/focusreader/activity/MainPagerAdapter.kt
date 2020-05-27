package com.github.deskid.focusreader.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import com.github.deskid.focusreader.screens.infoq.InfoQAndroidFragment
import com.github.deskid.focusreader.screens.penti.tugua.TuGuaFragment
import com.github.deskid.focusreader.screens.readhub.ReadhubFragment
import com.github.deskid.focusreader.screens.zhihudaily.ZhihuFragment

class MainPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
    private val registeredFragments = SparseArray<Fragment>()

    init {
        registeredFragments.put(0, ZhihuFragment.newInstance())
        registeredFragments.put(1, TuGuaFragment.newInstance())
        registeredFragments.put(2, InfoQAndroidFragment.newInstance())
        registeredFragments.put(3, ReadhubFragment.newInstance())
    }

    override fun getItem(position: Int): Fragment = registeredFragments[position]

    override fun getCount(): Int = registeredFragments.size()
}
