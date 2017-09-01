package com.github.deskid.focusreader.screens.penti

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.github.deskid.focusreader.screens.articledaily.ArticleDetailFragment

class ArticlePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ArticleDetailFragment.newInstance("day")
            1 -> ArticleDetailFragment.newInstance("random")
            else -> throw IllegalArgumentException("illegal index {$position}")
        }
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