package com.badoo.ribs.test.util

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import java.util.concurrent.TimeoutException

fun Activity.waitForDestroy() {
    waitFor { isDestroyed }
}

fun <T: Activity> ActivityTestRule<T>.waitForActivityFinish() {
    val activity = this.activity
    finishActivity()
    activity.waitForDestroy()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
}

fun waitFor(timeoutMillis: Long = 1000L, condition: () -> Boolean) {
    val start = System.currentTimeMillis()
    while (!condition()) {
        if (System.currentTimeMillis() > start + timeoutMillis) {
            throw TimeoutException("Condition is false after $timeoutMillis milliseconds")
        }

        Thread.sleep(1)
    }
}
