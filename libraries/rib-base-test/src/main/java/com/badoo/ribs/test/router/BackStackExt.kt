package com.badoo.ribs.test.router

import android.os.Parcelable
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.test.assertEquals
import com.badoo.ribs.test.assertInstanceOf
import com.badoo.ribs.test.assertTrue
import kotlin.reflect.KClass

fun <C : Parcelable> BackStack<C>.assertCurrentConfiguration(expected: C) {
    assertEquals(expected, state.current?.routing?.configuration)
}

inline fun <reified E : Parcelable> BackStack<*>.assertCurrentConfiguration() {
    assertInstanceOf(E::class, state.current?.routing?.configuration)
}

inline fun <reified C : Parcelable> BackStack<*>.assertCurrentConfiguration(crossinline predicate: (C) -> Boolean) {
    val config = state.current?.routing?.configuration
    assertInstanceOf(C::class, config)
    assertTrue(predicate(config as C)) { "$config does not satisfy requirements" }
}

fun <C : Parcelable> BackStack<C>.assertBackStack(vararg expected: C) {
    // BackStack is presented as simple list instead of stack, reverse expectations
    val reversed = expected.reversed()
    assertEquals(reversed, state.elements.map { it.routing.configuration })
}

fun BackStack<*>.assertBackStack(vararg expected: KClass<out Parcelable>) {
    // BackStack is presented as simple list instead of stack, reverse expectations
    val reversed = expected.reversed().map { it::class }
    assertEquals(reversed, state.elements.map { it.routing.configuration::class })
}
