package com.github.deskid.focusreader.widget.image

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

class WebImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatImageView(context, attrs) {

    private var mImageUrl: String? = null
    private var mResource: Int = 0

    override fun setImageResource(imageResource: Int) {
        super.setImageResource(imageResource)
        mResource = imageResource
        mImageUrl = null
    }
}
