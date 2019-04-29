package com.badoo.ribs.test.util

import android.support.test.InstrumentationRegistry

fun runOnMainSync(block: () -> Unit) = InstrumentationRegistry.getInstrumentation().runOnMainSync(block)
