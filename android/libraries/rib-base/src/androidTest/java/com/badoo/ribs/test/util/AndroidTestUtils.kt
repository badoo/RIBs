package com.badoo.ribs.test.util

import android.app.Activity
import android.support.test.rule.ActivityTestRule
import java.util.concurrent.TimeoutException

fun Activity.waitForDestroy(timeoutMillis: Long = 1000L) {
    val start = System.currentTimeMillis()
    while (!isDestroyed) {
        if (System.currentTimeMillis() > start + timeoutMillis) {
            throw TimeoutException("Activity is not destroyed in $timeoutMillis milliseconds")
        }

        Thread.sleep(1)
    }
}

fun <T: Activity> ActivityTestRule<T>.waitForActivityFinish() {
    val activity = this.activity
    finishActivity()
    activity.waitForDestroy()
}
