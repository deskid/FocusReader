package com.github.deskid.focusreader.screens.penti

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.deskid.focusreader.R
import kotlinx.android.synthetic.main.fragment_penti.*

class PentiFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_penti, container, false)
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        setUpViewPager()
    }

    private fun setUpViewPager() {
        pager.adapter = PentiPagerAdapter(childFragmentManager)
        pager.currentItem = 1
        tabLayout.setupWithViewPager(pager)
    }

    companion object {
        fun newInstance(): PentiFragment {
            return PentiFragment()
        }
    }

}