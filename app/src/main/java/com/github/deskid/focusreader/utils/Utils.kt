package com.github.deskid.focusreader.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ScrollView
import com.github.deskid.focusreader.R

/**
 * @author deskidzhoubo@didichuxing.com
 * @since  2019-10-22
 */
class Utils {
    companion object {

        /**
         * 节省每次创建时产生的开销，但要注意多线程操作synchronized
         */
        private val sCanvas = Canvas()


        /**
         * 显示截图结果
         *
         * @param view
         */
        fun showCaptureBitmap(view: View) {
            val dialog = AlertDialog.Builder(view.context)
//                .setView(R.layout.dialog_drawable_utils_createfromview)
                .setTitle("截图结果")
                .create()

            val contentView = LayoutInflater.from(view.context).inflate(R.layout.dialog_drawable_utils_createfromview, null)
            val displayImageView: ImageView? = contentView.findViewById(R.id.createFromViewDisplay)
            val createFromViewBitmap = createBitmapFromView(view, 1f)
            displayImageView?.setImageBitmap(createFromViewBitmap)

            displayImageView?.setOnClickListener(View.OnClickListener { dialog.dismiss() })

            dialog.show()
            dialog.setContentView(contentView)
        }

        fun showCaptureBitmap(view: WebView) {
            val dialog = AlertDialog.Builder(view.context)
//                .setView(R.layout.dialog_drawable_utils_createfromview)
                .setTitle("截图结果")
                .create()
            val contentView = LayoutInflater.from(view.context).inflate(R.layout.dialog_drawable_utils_createfromview, null)
            val displayImageView: ImageView? = contentView.findViewById(R.id.createFromViewDisplay)
            val createFromViewBitmap = createBitmapFromWebView(view, 1f)
            displayImageView?.setImageBitmap(createFromViewBitmap)

            displayImageView?.setOnClickListener(View.OnClickListener { dialog.dismiss() })

            dialog.show()
            dialog.setContentView(contentView)

        }

        fun createBitmapFromWebView(view: WebView, scale: Float): Bitmap? {
            view.clearFocus()
            val viewHeight = (view.contentHeight * view.scale).toInt()
            val bitmap = createBitmapSafely((view.width * scale).toInt(), (viewHeight * scale).toInt(), Bitmap.Config.ARGB_4444, 1)

            val unitHeight = view.height
            var bottom = viewHeight

            if (bitmap != null) {
                synchronized(sCanvas) {
                    val canvas = sCanvas
                    canvas.setBitmap(bitmap)
                    // 防止 View 上面有些区域空白导致最终 Bitmap 上有些区域变黑
                    canvas.drawColor(Color.WHITE)
                    canvas.scale(scale, scale)
                    while (bottom > 0) {
                        if (bottom < unitHeight) {
                            bottom = 0
                        } else {
                            bottom -= unitHeight
                        }
                        canvas.save()
                        canvas.clipRect(0, bottom, canvas.width, bottom + unitHeight)
                        view.scrollTo(0, bottom)
                        view.draw(canvas)
                        canvas.restore()
                    }
                    canvas.setBitmap(null)
                }
            }
            return bitmap
        }

        /**
         * 从一个view创建Bitmap。
         * 注意点：绘制之前要清掉 View 的焦点，因为焦点可能会改变一个 View 的 UI 状态。
         * 来源：https://github.com/tyrantgit/ExplosionField
         *
         * @param view  传入一个 View，会获取这个 View 的内容创建 Bitmap。
         * @param scale 缩放比例，对创建的 Bitmap 进行缩放，数值支持从 0 到 1。
         */
        fun createBitmapFromView(view: View, scale: Float): Bitmap? {
            if (view is ImageView) {
                val drawable = view.drawable
                if (drawable != null && drawable is BitmapDrawable) {
                    return drawable.bitmap
                }
            }
            view.clearFocus()
            var viewHeight = 0
            if (view is ScrollView) {
                for (i in 0 until view.childCount) {
                    viewHeight += view.getChildAt(i).height
                }
            } else if (view is NestedScrollView) {
                for (i in 0 until view.childCount) {
                    viewHeight += view.getChildAt(i).height
                }
            } else {
                viewHeight = view.height
            }

            val bitmap = createBitmapSafely((view.width * scale).toInt(),
                (viewHeight * scale).toInt(), Bitmap.Config.ARGB_8888, 1)
            if (bitmap != null) {
                synchronized(sCanvas) {
                    val canvas = sCanvas
                    canvas.setBitmap(bitmap)
                    canvas.save()
                    // 防止 View 上面有些区域空白导致最终 Bitmap 上有些区域变黑
                    canvas.drawColor(Color.WHITE)
                    canvas.scale(scale, scale)
                    view.draw(canvas)
                    canvas.restore()
                    canvas.setBitmap(null)
                }
            }
            return bitmap
        }

        /**
         * 安全的创建bitmap。
         * 如果新建 Bitmap 时产生了 OOM，可以主动进行一次 GC - System.gc()，然后再次尝试创建。
         *
         * @param width      Bitmap 宽度。
         * @param height     Bitmap 高度。
         * @param config     传入一个 Bitmap.Config。
         * @param retryCount 创建 Bitmap 时产生 OOM 后，主动重试的次数。
         * @return 返回创建的 Bitmap。
         */
        fun createBitmapSafely(width: Int, height: Int, config: Bitmap.Config, retryCount: Int): Bitmap? {
            //width and height must be > 0
            if (width <= 0 || height <= 0) {
                return null
            }
            try {
                return Bitmap.createBitmap(width, height, config)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                if (retryCount > 0) {
                    System.gc()
                    return createBitmapSafely(width, height, config, retryCount - 1)
                }
                return null
            }

        }
    }
}