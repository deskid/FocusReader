package com.github.deskid.focusreader.screens.penti

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.github.deskid.focusreader.screens.penti.duanzi.DuanziFragment
import com.github.deskid.focusreader.screens.penti.tugua.TuGuaFragment
import com.github.deskid.focusreader.screens.penti.yitu.ZenImageFragment

class PentiPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ZenImageFragment.newInstance()
            1 -> TuGuaFragment.newInstance()
            2 -> DuanziFragment.newInstance()
            else -> throw IllegalArgumentException("illegal index {$position}")
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "意图"
            1 -> "图卦"
            2 -> "段子"
            else -> throw IllegalArgumentException("illegal index {$position}")
        }
    }
}