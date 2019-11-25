package com.github.deskid.focusreader.widget.image

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.widget.image.glide.GlideApp
import com.github.logutils.LogUtils

fun WebImageView.setImageUrl(url: String?) {
    if (url.isNullOrEmpty()) {
        setImageBitmap(null)
        return
    }

    if (url.endsWith(".gif")) {
        GlideApp.with(context)
                .asGif()
                .load(url)
                .placeholder(R.drawable.bg_img)
                .error(R.drawable.bg_img)
                .into(this)
    } else {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.bg_img)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(R.anim.alpha_in))
                .error(R.drawable.bg_img)
                .into(this)
    }
}

typealias OnPaletteLoaded = (palette: Palette?) -> Unit
typealias OnImageLoaded = (resource: Bitmap?) -> Unit

fun WebImageView.setImageUrl(url: String?, onPaletteLoaded: OnPaletteLoaded? = null, onImageLoaded: OnImageLoaded? = null) {
    if (url.isNullOrEmpty()) {
        setImageBitmap(null)
        return
    }
    if (url.endsWith(".gif")) {
        GlideApp.with(context)
                .asGif()
                .load(url)
                .placeholder(R.drawable.bg_img)
                .error(R.drawable.bg_img)
                .into(this)
    } else {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.bg_img)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(R.anim.alpha_in))
                .error(R.drawable.bg_img)
                .into(object : BitmapImageViewTarget(this) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        super.onResourceReady(resource, transition)
                        LogUtils.d("onResourceReady")
                        onImageLoaded?.invoke(resource)
                        onPaletteLoaded?.let {
                            resource.let {
                                Palette.from(it).maximumColorCount(10).generate { palette ->
                                    onPaletteLoaded.invoke(palette)
                                }
                            }
                        }
                    }
                })
    }
}


