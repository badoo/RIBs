package com.badoo.ribs.test.util

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import java.util.concurrent.TimeoutException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

private const val DEFAULT_CONDITION_TIMEOUT_MILLISECONDS = 15000L
private const val DEFAULT_CHECK_CONDITION_FREQUENCY_MILLIS = 10L

fun Activity.waitForDestroy() {
    waitFor { isDestroyed }
}

fun <T : Activity> ActivityTestRule<T>.finishActivitySync() {
    val activity = this.activity
    finishActivity()
    activity.waitForDestroy()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
}

fun <T : Activity> ActivityTestRule<T>.restartActivitySync() {
    val resumedLatch = CountDownLatch(1)
    ActivityLifecycleMonitorRegistry.getInstance()
        .addLifecycleCallback { _, stage ->
            if (stage == Stage.RESUMED) {
                resumedLatch.countDown()
            }
        }

    runOnMainSync {
        activity.recreate()
    }

    resumedLatch.await(DEFAULT_CONDITION_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
}

fun waitForIdle(block: () -> Unit) = InstrumentationRegistry.getInstrumentation().waitForIdle(block)

fun runOnMainSync(block: () -> Unit) = InstrumentationRegistry.getInstrumentation().runOnMainSync(block)

fun waitFor(timeoutMillis: Long = DEFAULT_CONDITION_TIMEOUT_MILLISECONDS, condition: () -> Boolean) {
    val start = System.currentTimeMillis()
    while (!condition()) {
        if (System.currentTimeMillis() > start + timeoutMillis) {
            throw TimeoutException("Condition is false after $timeoutMillis milliseconds")
        }

        Thread.sleep(DEFAULT_CHECK_CONDITION_FREQUENCY_MILLIS)
    }
}
