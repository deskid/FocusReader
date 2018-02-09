package com.github.deskid.focusreader.widget.image

import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.widget.image.glide.GlideApp

fun WebImageView.setImageUrl(url: String?) {
    if (url.isNullOrEmpty()) {
        setImageBitmap(null)
        return
    }

    if (url!!.endsWith(".gif")) {
        GlideApp.with(context)
                .asGif()
                .load(url)
                .placeholder(R.drawable.bg_img)
                .centerCrop()
                .error(R.drawable.bg_img)
                .into(this)
    } else {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.bg_img)
                .centerCrop()
                .error(R.drawable.bg_img)
                .into(this)
    }
}

fun WebImageView.setImageUrl(url: String?, onPaletteLoaded: ((palette: Palette?) -> Unit)? = null, onImageLoaded: ((resource: Bitmap?) -> Unit)? = null) {
    if (url.isNullOrEmpty()) {
        setImageBitmap(null)
        return
    }
    if (url!!.endsWith(".gif")) {
        GlideApp.with(context)
                .asGif()
                .load(url)
                .placeholder(R.drawable.bg_img)
                .centerCrop()
                .error(R.drawable.bg_img)
                .into(this)
    } else {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.bg_img)
                .thumbnail(0.5f)
                .centerCrop()
                .error(R.drawable.bg_img)
                .into(object : BitmapImageViewTarget(this) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        super.onResourceReady(resource, transition)
                        onImageLoaded?.invoke(resource)
                        onPaletteLoaded?.let {
                            resource.let {
                                Palette.from(it).maximumColorCount(4).generate { palette ->
                                    onPaletteLoaded.invoke(palette)
                                }
                            }
                        }
                    }
                })
    }
}


