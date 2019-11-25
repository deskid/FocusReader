package com.github.deskid.focusreader.screens.penti.yitu

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.github.chrisbanes.photoview.PhotoView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.utils.screenHeight
import com.github.deskid.focusreader.utils.screenWidth

class ZenImagePhotoViewDlg(context: Context, var bitmap: Bitmap?) : Dialog(context, R.style.transparentBgDialog) {

    var photoView: PhotoView

    init {
        val view = View.inflate(context, R.layout.dialog_zenimage_detail, null)
        photoView = view.findViewById(R.id.photo_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(photoView)

        window?.let {
            val wl = it.attributes
            wl.x = 0
            wl.y = 0
            wl.height = context.screenHeight
            wl.width = context.screenWidth
            wl.gravity = Gravity.CENTER
            it.attributes = wl
        }


        photoView.setOnPhotoTapListener { _, _, _ -> dismiss() }
        photoView.setImageBitmap(bitmap)
    }
}
