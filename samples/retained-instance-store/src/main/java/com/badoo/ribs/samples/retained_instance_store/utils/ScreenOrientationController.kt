package com.badoo.ribs.samples.retained_instance_store.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration

class ScreenOrientationController(private val activity: Activity){

    fun toggleScreenOrientation() {
        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

}