package com.badoo.ribs.core.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.source.backstack.BackStackFeature
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import com.badoo.ribs.core.routing.source.backstack.BackStack

class Remove<C : Parcelable>(
    private val identifier: Routing.Identifier
) : BackStackOperation<C> {

    override fun isApplicable(backStack: BackStack<C>): Boolean =
        backStack.hasContentWithIdentifier() ||
        backStack.hasOverlayWithIdentifier()

    private fun BackStack<C>.hasContentWithIdentifier() =
        find { it.routing.identifier == identifier } != null

    private fun BackStack<C>.hasOverlayWithIdentifier() =
        any { it.overlays.find { it.identifier == identifier } != null }

    override fun invoke(backStack: BackStack<C>): BackStack<C> = when {
        backStack.hasContentWithIdentifier() -> removeContent(backStack)
        backStack.hasOverlayWithIdentifier() -> removeOverlay(backStack)
        else -> backStack
    }

    private fun removeContent(backStack: BackStack<C>): List<RoutingHistoryElement<C>> {
        val toRemove = backStack.find {
            it.routing.identifier == identifier
        }

        requireNotNull(toRemove)
        return backStack.minus(toRemove)
    }

    private fun removeOverlay(backStack: BackStack<C>): MutableList<RoutingHistoryElement<C>> {
        lateinit var overlayToRemove: Routing<C>
        var indexOfElementContainingOverlay: Int = -1

        backStack.forEachIndexed { idx, element ->
            val found = element.overlays.find {
                it.identifier == identifier
            }

            if (found != null) {
                overlayToRemove = found
                indexOfElementContainingOverlay = idx
            }
        }

        val element = backStack[indexOfElementContainingOverlay]
        val mutable = backStack.toMutableList()
        val modified = element.copy(
            overlays = element.overlays.minus(overlayToRemove)
        )

        mutable[indexOfElementContainingOverlay] = modified

        return mutable
    }
}


fun <C : Parcelable> BackStackFeature<C>.remove(identifier: Routing.Identifier) {
    accept(BackStackFeature.Operation(Remove(identifier)))
}
