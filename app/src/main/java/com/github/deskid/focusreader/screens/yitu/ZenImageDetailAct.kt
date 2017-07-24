package com.github.deskid.focusreader.screens.yitu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.animation.AnimationUtils
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.ZenImage
import com.github.deskid.focusreader.app.App
import kotlinx.android.synthetic.main.activity_zenitem_detail.*

class ZenImageDetailAct : AppCompatActivity() {

    companion object {
        fun start(context: Context, initPosition: Int, images: ArrayList<ZenImage>) {
            val starter = Intent(context, ZenImageDetailAct::class.java)
            starter.putExtra("INITPOSITION", initPosition)
            starter.putParcelableArrayListExtra("IMAGES", images)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zenitem_detail)
        (applicationContext as App).appComponent.inject(this)

        val transitions = TransitionSet()
        val slide = Slide(Gravity.BOTTOM)
        slide.interpolator = AnimationUtils.loadInterpolator(this,
                android.R.interpolator.linear_out_slow_in)
        slide.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        transitions.addTransition(slide)
        transitions.addTransition(Fade())
        window.enterTransition = transitions

        setUpViewPager(intent.getIntExtra("INITPOSITION", 0), intent.getParcelableArrayListExtra("IMAGES"))
    }

    private fun setUpViewPager(initPosition: Int, images: List<ZenImage>) {
        pager.adapter = ZenImageDetailPagerAdapter(images, supportFragmentManager)
        pager.currentItem = initPosition
        pager.offscreenPageLimit = 5
    }
}