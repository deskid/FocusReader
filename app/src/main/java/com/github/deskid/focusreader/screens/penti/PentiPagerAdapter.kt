package com.github.deskid.focusreader.screens.penti

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.github.deskid.focusreader.screens.duanzi.DuanziFragment
import com.github.deskid.focusreader.screens.tugua.TuGuaFragment
import com.github.deskid.focusreader.screens.yitu.ZenImageFragment

class PentiPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return ZenImageFragment.newInstance()
            1 -> return TuGuaFragment.newInstance()
            2 -> return DuanziFragment.newInstance()
            else -> throw IllegalArgumentException("illegal index {$position}")
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return "意图"
            1 -> return "图卦"
            2 -> return "段子"
            else -> throw IllegalArgumentException("illegal index {$position}")
        }
    }
}