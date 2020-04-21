package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement

data class PushOverlay<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: BackStack<C>): Boolean =
        backStack.isNotEmpty() && configuration != backStack.currentOverlay

    override fun invoke(backStack: BackStack<C>): BackStack<C> =
        backStack.replaceLastWith(
            backStack.last().copy(
                overlays = backStack.last().overlays + configuration
            )
        )

    private val BackStack<C>.current: BackStackElement<C>?
        get() = this.lastOrNull()

    private val BackStack<C>.currentOverlay: C?
        get() = current?.overlays?.lastOrNull()

    private fun BackStack<C>.replaceLastWith(replacement: BackStackElement<C>): BackStack<C> =
        toMutableList().apply { set(lastIndex, replacement) }
}

fun <C : Parcelable, Overlay : C> Router<C, *, *, Overlay, *>.pushOverlay(configuration: Overlay) {
    acceptOperation(PushOverlay(configuration))
}
