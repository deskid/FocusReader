package com.github.deskid.focusreader.screens.readhub

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import com.github.deskid.focusreader.screens.readhub.news.NewsFragment
import com.github.deskid.focusreader.screens.readhub.technews.TechnewsFragment
import com.github.deskid.focusreader.screens.readhub.topic.TopicFragment

class ReadhubPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragments = SparseArray<Fragment?>()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                if (fragments.get(0) == null) fragments.put(0, TopicFragment.newInstance())
                fragments[0]!!
            }
            1 -> {
                if (fragments.get(1) == null) fragments.put(1, NewsFragment.newInstance())
                fragments[1]!!
            }
            2 -> {
                if (fragments.get(2) == null) fragments.put(2, TechnewsFragment.newInstance())
                fragments[2]!!
            }
            else -> throw IllegalArgumentException("illegal index {$position}")
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "热门话题"
            1 -> "科技动态"
            2 -> "开发者资讯"
            else -> throw IllegalArgumentException("illegal index {$position}")
        }
    }
}