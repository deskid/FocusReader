package com.github.deskid.focusreader.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.customtabs.CustomTabsClient
import android.support.customtabs.CustomTabsIntent
import com.github.deskid.focusreader.R

@SuppressLint("PrivateResource")
fun Context.launchUrlWithCustomTabs(uri: Uri) {
    var builder = CustomTabsIntent.Builder()

    builder.setToolbarColor(getColorCompat(R.color.colorPrimary))

//    builder.setStartAnimations(this, R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
//    builder.setExitAnimations(this, R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom)
    builder.setCloseButtonIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_arrow_back))
    builder.setShowTitle(true)

//    val menuIntent = Intent()
//    menuIntent.setClass(applicationContext, MainActivity::class.java)
//    val pi = PendingIntent.getActivity(applicationContext, 0, menuIntent, 0)
//    builder.addMenuItem("Menu entry 1", pi)

    builder.addDefaultShareMenuItem()
    val customTabsIntent = builder.build()
    var intent = customTabsIntent.intent
    intent.data = uri


    startActivity(intent, customTabsIntent.startAnimationBundle)
}

fun Context.launchUrlWithCustomTabs(uriString: String) {
    launchUrlWithCustomTabs(Uri.parse(uriString))
}

fun Activity.warmUpCustomTabs() {
    CustomTabsClient.connectAndInitialize(this, "com.android.chrome")
}