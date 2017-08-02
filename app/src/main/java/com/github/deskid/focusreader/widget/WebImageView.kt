package com.github.deskid.focusreader.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.graphics.Palette
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.github.deskid.focusreader.R
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette

class WebImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatImageView(context, attrs) {

    private var mImageUrl: String? = null
    private var mResource: Int = 0

    override fun setImageResource(imageResource: Int) {
        super.setImageResource(imageResource)
        mResource = imageResource
        mImageUrl = null
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
    }

    fun setImageUrl(url: String?, onLoaded: ((palette: Palette?) -> Unit)? = null) {
        if (url.isNullOrEmpty()) {
            mImageUrl = null
            setImageBitmap(null)
        } else {
            mImageUrl = url
            if (isAttachedToWindow) {
                val option = RequestOptions()
                        .placeholder(R.color.colorGray)
                        .priority(Priority.HIGH)

                if (onLoaded == null) {
                    Glide.with(this)
                            .load(url)
                            .apply(option)
                            .into(this)
                } else {
                    Glide.with(this)
                            .load(url)
                            .apply(option)
                            .listener(
                                    GlidePalette
                                            .with(url)
                                            .use(BitmapPalette.Profile.MUTED_DARK)
                                            .intoCallBack(onLoaded))
                            .into(this)
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!mImageUrl.isNullOrEmpty()) {
            setImageUrl(mImageUrl!!)
        } else if (mResource != -1) {
            setImageResource(mResource)
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }
}
