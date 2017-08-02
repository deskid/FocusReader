package com.github.deskid.focusreader.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import com.github.deskid.focusreader.screens.articledaily.ArticleFragment
import com.github.deskid.focusreader.screens.penti.PentiFragment
import com.github.deskid.focusreader.screens.zhihudaily.ZhihuFragment

class MainPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
    private val registeredFragments = SparseArray<Fragment>()

    init {
        registeredFragments.put(0, ZhihuFragment.newInstance())
        registeredFragments.put(1, PentiFragment.newInstance())
        registeredFragments.put(2, ArticleFragment.newInstance())
    }

    override fun getItem(position: Int): Fragment = registeredFragments[position]

    override fun getCount(): Int = registeredFragments.size()
}