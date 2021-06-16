package com.badoo.ribs.test

import kotlin.reflect.KClass

fun assertTrue(check: Boolean, error: () -> String) {
    if (!check) throw AssertionError(error())
}

fun <T> assertEquals(expected: T, actual: T, error: (() -> String)? = null) {
    if (actual != expected) {
        throw AssertionError(error?.invoke() ?: "Expected: $expected, but received $actual")
    }
}

fun assertInstanceOf(expected: KClass<*>, actual: Any?) {
    if (!expected.isInstance(actual)) {
        throw AssertionError("Expected $actual to be instance of ${expected.qualifiedName}")
    }
}
