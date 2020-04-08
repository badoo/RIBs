package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature

data class PushOverlayBackStackOperation<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: List<BackStackElement<C>>): Boolean =
        backStack.isNotEmpty() && configuration != backStack.currentOverlay

    override fun modifyStack(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> =
        backStack.replaceLastWith(
            backStack.last().copy(
                overlays = backStack.last().overlays + configuration
            )
        )

    private val List<BackStackElement<C>>.current: BackStackElement<C>?
        get() = this.lastOrNull()

    private val List<BackStackElement<C>>.currentOverlay: C?
        get() = current?.overlays?.lastOrNull()

    private fun List<BackStackElement<C>>.replaceLastWith(replacement: BackStackElement<C>): List<BackStackElement<C>> =
        toMutableList().apply { set(lastIndex, replacement) }
}

fun <C : Parcelable, Overlay : C> Router<C, *, *, Overlay, *>.pushOverlay(configuration: Overlay) {
    acceptOperation(PushOverlayBackStackOperation(configuration))
}

internal fun <C : Parcelable> PushOverlay(configuration: C) =
    BackStackFeature.Operation.ExtendedOperation(PushOverlayBackStackOperation(configuration))