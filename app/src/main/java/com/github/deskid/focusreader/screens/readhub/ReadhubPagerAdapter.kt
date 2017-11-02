package com.github.deskid.focusreader.screens.readhub

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.github.deskid.focusreader.screens.readhub.technews.NewsFragment
import com.github.deskid.focusreader.screens.readhub.technews.TechnewsFragment
import com.github.deskid.focusreader.screens.readhub.topic.TopicFragment

class ReadhubPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TopicFragment.newInstance()
            1 -> NewsFragment.newInstance()
            2 -> TechnewsFragment.newInstance()
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