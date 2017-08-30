package com.github.deskid.focusreader.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.graphics.Palette
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
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

    fun setImageUrl(url: String?, onPaletteLoaded: ((palette: Palette?) -> Unit)? = null, onImageLoaded: (() -> Unit)? = null) {
        Log.d("TAG", "onPaletteLoaded=" + onPaletteLoaded + "onImageLoaded=" + onImageLoaded)
        if (url.isNullOrEmpty()) {
            mImageUrl = null
            setImageBitmap(null)
        } else {
            mImageUrl = url

            val option = RequestOptions()
                    .dontAnimate()
                    .priority(Priority.HIGH)

            val requestListener = object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    onImageLoaded?.invoke()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    onImageLoaded?.invoke()
                    return false
                }
            }

            if (onPaletteLoaded == null) {
                Glide.with(this)
                        .load(url)
                        .apply(option)
                        .listener(requestListener)
                        .into(this)
            } else {
                Glide.with(this)
                        .load(url)
                        .apply(option)
                        .listener(
                                GlidePalette
                                        .with(url)
                                        .use(BitmapPalette.Profile.MUTED_DARK)
                                        .setGlideListener(requestListener)
                                        .intoCallBack(onPaletteLoaded))
                        .into(this)
            }
        }
    }
}
