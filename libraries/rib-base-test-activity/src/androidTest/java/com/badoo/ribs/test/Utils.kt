package com.badoo.ribs.test

import java.util.concurrent.TimeoutException

// TODO Extract and reuse with rib-base

private const val DEFAULT_CONDITION_TIMEOUT_MILLISECONDS = 15000L
private const val DEFAULT_CHECK_CONDITION_FREQUENCY_MILLIS = 10L

fun waitFor(timeoutMillis: Long = DEFAULT_CONDITION_TIMEOUT_MILLISECONDS, condition: () -> Boolean) {
    val start = System.currentTimeMillis()
    while (!condition()) {
        if (System.currentTimeMillis() > start + timeoutMillis) {
            throw TimeoutException("Condition is false after $timeoutMillis milliseconds")
        }

        Thread.sleep(DEFAULT_CHECK_CONDITION_FREQUENCY_MILLIS)
    }
}
