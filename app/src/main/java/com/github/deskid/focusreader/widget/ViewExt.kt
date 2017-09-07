package com.github.deskid.focusreader.widget

import android.view.View
import android.view.ViewPropertyAnimator

fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
}

fun View.setWidth(width: Int) {
    val params = layoutParams
    params.width = width
    layoutParams = params
}

fun View.slideExit() {
    if (translationY == 0f) animate().translationY(-height.toFloat())
}

fun View.slideEnter() {
    if (translationY < 0f) animate().translationY(0f)
}

fun View.fadeIn(duration: Long = 400): ViewPropertyAnimator? {
    return animate()
            .alpha(1.0f)
            .setDuration(duration)
}

fun View.fadeOut(duration: Long = 400): ViewPropertyAnimator? {
    return animate()
            .alpha(0.0f)
            .setDuration(duration)
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.toggle() {
    visibility = if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
}