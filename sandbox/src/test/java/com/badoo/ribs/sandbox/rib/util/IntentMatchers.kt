package com.badoo.ribs.sandbox.rib.util

import android.content.Intent
import org.assertj.core.api.Condition

inline fun <reified T> component(): Condition<Intent> =
    object : Condition<Intent>("has component with specified class") {
        override fun matches(value: Intent?): Boolean {
            return value?.component?.className == T::class.java.name
        }
    }
