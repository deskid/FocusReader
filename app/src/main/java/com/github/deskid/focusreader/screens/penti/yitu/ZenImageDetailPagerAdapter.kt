package com.github.deskid.focusreader.screens.penti.yitu

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import com.github.deskid.focusreader.api.data.ZenImage

class ZenImageDetailPagerAdapter(private val images: List<ZenImage>, fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragments = SparseArray<Fragment?>()

    override fun getItem(position: Int): Fragment {
        val zenImage = images[position]
        if (fragments.get(position) == null) {
            fragments.put(position, ZenImageDetailFragment.newInstance(zenImage.url, zenImage.imgurl))
        }
        return fragments.get(position)!!
    }

    override fun getCount(): Int = images.size

}