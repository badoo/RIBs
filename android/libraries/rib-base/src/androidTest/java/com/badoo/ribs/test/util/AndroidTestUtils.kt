package com.badoo.ribs.test.util

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
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

fun <T: Activity> ActivityTestRule<T>.waitForActivityRestart() {
    InstrumentationRegistry.getInstrumentation()
        .runOnMainSync {
            activity.recreate()
        }

    var activityResumed = false
    ActivityLifecycleMonitorRegistry.getInstance()
        .addLifecycleCallback { _, stage ->
            if (stage == Stage.RESUMED) {
                activityResumed = true
            }
        }
    waitFor { activityResumed }
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
