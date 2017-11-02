package com.github.deskid.focusreader.screens.penti.yitu

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.github.deskid.focusreader.api.data.ZenImage

class ZenImageDetailPagerAdapter(val images: List<ZenImage>, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val zenImage = images[position]
        return ZenImageDetailFragment.newInstance(zenImage.url, zenImage.imgurl)
    }

    override fun getCount(): Int = images.size

}