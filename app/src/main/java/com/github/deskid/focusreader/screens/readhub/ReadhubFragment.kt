package com.github.deskid.focusreader.screens.readhub

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.deskid.focusreader.R
import kotlinx.android.synthetic.main.fragment_readhub.*

class ReadhubFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_readhub, container, false)
    }

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        setUpViewPager()
        toolbar.title = Html.fromHtml(getString(R.string.readhub_title))
    }

    private fun setUpViewPager() {
        pager.adapter = ReadhubPagerAdapter(childFragmentManager)
        pager.currentItem = 0
        tabLayout.setupWithViewPager(pager)
    }

    companion object {
        fun newInstance(): ReadhubFragment {
            return ReadhubFragment()
        }
    }

}