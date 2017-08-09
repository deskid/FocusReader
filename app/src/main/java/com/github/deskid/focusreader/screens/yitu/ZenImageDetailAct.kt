package com.github.deskid.focusreader.screens.yitu

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.*
import android.util.Pair
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.ZenImage
import com.github.deskid.focusreader.app.App
import kotlinx.android.synthetic.main.activity_zenitem_detail.*

class ZenImageDetailAct : AppCompatActivity() {

    private var initPosition: Int = 0
    private lateinit var images: List<ZenImage>

    companion object {
        val KEY_SELECTED_POSITION = "SELECTED_POSITION"

        fun start(activity: Activity, initPosition: Int, imageView: ImageView, images: ArrayList<ZenImage>) {
            val starter = Intent(activity, ZenImageDetailAct::class.java)
            starter.putExtra(KEY_SELECTED_POSITION, initPosition)
            starter.putParcelableArrayListExtra("IMAGES", images)

            val decorView = activity.window.decorView
            val statusBackground: View = decorView.findViewById(android.R.id.statusBarBackground)
            val statusPair = Pair.create(statusBackground,
                    statusBackground.transitionName)
            val imagePair = Pair.create(imageView as View, imageView.transitionName)

            val options = ActivityOptions.makeSceneTransitionAnimation(activity, imagePair, statusPair).toBundle()
            activity.startActivityForResult(starter, 0, options)
        }
    }

    fun setTransition() {
        postponeEnterTransition()

        // material-themed applications have their enter/return content transitions
        // started a tiny bit before their exit/reenter content transitions complete,
        // creating a small overlap that makes the overall effect more seamless and dramatic
        window.allowEnterTransitionOverlap = false
        window.allowReturnTransitionOverlap = false
//
        window.enterTransition = Slide(Gravity.BOTTOM).apply {
            duration = 200
        }

        val entertransitionSet = TransitionSet().apply {
            addTransition(ChangeImageTransform())
            addTransition(ChangeBounds())
            addTransition(Fade(Fade.IN))
        }

        val returntransitionSet = TransitionSet().apply {
            addTransition(ChangeImageTransform())
            addTransition(ChangeBounds())
            addTransition(Fade(Fade.IN))
        }

        window.returnTransition = Slide(Gravity.BOTTOM).apply {
            duration = 200
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTransition()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zenitem_detail)
        (applicationContext as App).appComponent.inject(this)

        initPosition = intent.getIntExtra(KEY_SELECTED_POSITION, 0)
        images = intent.getParcelableArrayListExtra("IMAGES")

        setUpViewPager(initPosition, images)
    }

    private fun setUpViewPager(initPosition: Int, images: List<ZenImage>) {
        pager.adapter = ZenImageDetailPagerAdapter(images, supportFragmentManager)
        pager.currentItem = initPosition
        pager.offscreenPageLimit = 5
    }

    override fun onBackPressed() {
        finishAfterTransition()
    }

    override fun finishAfterTransition() {
        setActivityResult()
        super.finishAfterTransition()
    }

    private fun setActivityResult() {
        if (initPosition == pager.currentItem) {
            setResult(Activity.RESULT_OK)
            return
        } else {
            val intent = Intent()
            intent.putExtra(KEY_SELECTED_POSITION, pager.currentItem)
            setResult(Activity.RESULT_OK, intent)
        }
    }
}