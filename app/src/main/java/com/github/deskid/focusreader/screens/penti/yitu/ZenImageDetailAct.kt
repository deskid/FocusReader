package com.github.deskid.focusreader.screens.penti.yitu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.ZenImage
import kotlinx.android.synthetic.main.activity_zenitem_detail.*

class ZenImageDetailAct : AppCompatActivity() {

    private var initPosition: Int = 0
    private lateinit var images: List<ZenImage>

    companion object {
        const val KEY_SELECTED_POSITION = "SELECTED_POSITION"

        fun start(activity: Activity, initPosition: Int, images: ArrayList<ZenImage>, options: Bundle? = null) {
            val starter = Intent(activity, ZenImageDetailAct::class.java)
            starter.putExtra(KEY_SELECTED_POSITION, initPosition)
            starter.putParcelableArrayListExtra("IMAGES", images)

            activity.startActivityForResult(starter, 0, options)
        }
    }

    private fun setTransition() {
        postponeEnterTransition()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTransition()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zenitem_detail)

        initPosition = intent.getIntExtra(KEY_SELECTED_POSITION, 0)
        images = intent.getParcelableArrayListExtra("IMAGES")

        setUpViewPager(initPosition, images)
    }

    private fun setUpViewPager(initPosition: Int, images: List<ZenImage>) {
        pager.adapter = ZenImageDetailPagerAdapter(images, supportFragmentManager)
        pager.currentItem = initPosition
        pager.offscreenPageLimit = 1
    }

    override fun finishAfterTransition() {
        setActivityResult()
        super.finishAfterTransition()
    }

    private fun setActivityResult() {
        if (initPosition == pager.currentItem) {
            setResult(Activity.RESULT_OK)
        } else {
            val intent = Intent()
            intent.putExtra(KEY_SELECTED_POSITION, pager.currentItem)
            setResult(Activity.RESULT_OK, intent)
        }
    }
}