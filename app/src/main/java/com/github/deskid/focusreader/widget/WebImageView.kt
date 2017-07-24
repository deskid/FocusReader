package com.github.deskid.focusreader.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.github.deskid.focusreader.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

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

    fun setImageUrl(url: String?, onLoaded: ((bitmap: Bitmap) -> Unit)? = null) {
        if (url.isNullOrEmpty()) {
            mImageUrl = null
            Picasso.with(context).cancelRequest(this)
            setImageBitmap(null)
        } else {
            mImageUrl = url
            if (isAttachedToWindow) {
                Picasso.with(context).load(Uri.parse(url)).placeholder(R.color.colorAccent).into(object : Target {
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        setImageDrawable(placeHolderDrawable)
                    }

                    override fun onBitmapFailed(errorDrawable: Drawable?) {
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                        setImageBitmap(bitmap)
                        onLoaded?.invoke(bitmap)
                    }
                })
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
        Picasso.with(context).cancelRequest(this)
        super.onDetachedFromWindow()
    }
}
