package com.github.deskid.focusreader.screens.penti

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import com.github.deskid.focusreader.screens.penti.duanzi.DuanziFragment
import com.github.deskid.focusreader.screens.penti.tugua.TuGuaFragment

class PentiPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = SparseArray<Fragment?>()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                if (fragments.get(0) == null) fragments.put(0, TuGuaFragment.newInstance())
                fragments[0]!!
            }
            else -> throw IllegalArgumentException("illegal index {$position}")
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "图卦"
            else -> throw IllegalArgumentException("illegal index {$position}")
        }
    }
}
