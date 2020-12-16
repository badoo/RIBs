package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.Elements

/**
 * Operation:
 *
 * [A, B, C] + Remove(id of B) = [A, C]
 */
class Remove<C : Parcelable>(
    private val identifier: Routing.Identifier
) : BackStack.Operation<C> {

    override fun isApplicable(elements: Elements<C>): Boolean =
        elements.hasContentWithIdentifier() ||
        elements.hasOverlayWithIdentifier()

    private fun Elements<C>.hasContentWithIdentifier() =
        find { it.routing.identifier == identifier } != null

    private fun Elements<C>.hasOverlayWithIdentifier() =
        any { it.overlays.find { it.identifier == identifier } != null }

    override fun invoke(elements: Elements<C>): Elements<C> = when {
        elements.hasContentWithIdentifier() -> removeContent(elements)
        elements.hasOverlayWithIdentifier() -> removeOverlay(elements)
        else -> elements
    }

    private fun removeContent(elements: Elements<C>): List<RoutingHistoryElement<C>> {
        val toRemove = elements.find {
            it.routing.identifier == identifier
        }

        requireNotNull(toRemove)
        return elements.minus(toRemove)
    }

    private fun removeOverlay(elements: Elements<C>): MutableList<RoutingHistoryElement<C>> {
        lateinit var overlayToRemove: Routing<C>
        var indexOfElementContainingOverlay: Int = -1

        elements.forEachIndexed { idx, element ->
            val found = element.overlays.find {
                it.identifier == identifier
            }

            if (found != null) {
                overlayToRemove = found
                indexOfElementContainingOverlay = idx
            }
        }

        val element = elements[indexOfElementContainingOverlay]
        val mutable = elements.toMutableList()
        val modified = element.copy(
            overlays = element.overlays.minus(overlayToRemove)
        )

        mutable[indexOfElementContainingOverlay] = modified

        return mutable
    }
}


fun <C : Parcelable> BackStack<C>.remove(identifier: Routing.Identifier) {
    accept(Remove(identifier))
}
