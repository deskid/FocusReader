package com.github.deskid.focusreader.screens.penti

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import com.github.deskid.focusreader.screens.penti.duanzi.DuanziFragment
import com.github.deskid.focusreader.screens.penti.tugua.TuGuaFragment
import com.github.deskid.focusreader.screens.penti.yitu.ZenImageFragment

class PentiPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = SparseArray<Fragment?>()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                if (fragments.get(0) == null) fragments.put(0, ZenImageFragment.newInstance())
                fragments[0]!!
            }
            1 -> {
                if (fragments.get(1) == null) fragments.put(1, TuGuaFragment.newInstance())
                fragments[1]!!
            }
            2 -> {
                if (fragments.get(2) == null) fragments.put(2, DuanziFragment.newInstance())
                fragments[2]!!
            }
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