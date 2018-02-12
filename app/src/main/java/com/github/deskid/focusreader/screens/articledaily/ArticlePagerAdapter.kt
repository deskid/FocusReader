package com.github.deskid.focusreader.screens.articledaily

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray

class ArticlePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragments = SparseArray<Fragment?>()
    private val types = arrayOf("day", "random")

    override fun getItem(position: Int): Fragment {
        if (fragments.get(position) == null) {
            fragments.put(position, ArticleDetailFragment.newInstance(types[position]))
        }
        return fragments.get(position)!!
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "今日"
            1 -> "随机"
            else -> throw IllegalArgumentException("illegal index {$position}")
        }
    }
}