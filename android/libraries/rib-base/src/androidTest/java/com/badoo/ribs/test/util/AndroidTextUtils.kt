package com.badoo.ribs.test.util

import android.app.Activity
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
