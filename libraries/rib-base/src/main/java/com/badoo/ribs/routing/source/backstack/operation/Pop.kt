package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.Elements

/**
 * Operation:
 *
 * [A, B, C] + Pop = [A, B]  // no overlays
 * [A, B, C {O1}] + Pop = [A, B, C]  // overlays are popped first
 */
class Pop<C : Parcelable> : BackStack.Operation<C> {

    override fun invoke(elements: Elements<C>): Elements<C> =
        when {
            elements.canPopOverlay ->
                elements.replaceLastWith(
                    elements.last().copy(
                        overlays = elements.last().overlays.dropLast(1)
                    )
                )
            elements.canPopContent -> elements.dropLast(1)
            else -> elements
        }

    override fun isApplicable(elements: Elements<C>): Boolean =
        when {
            elements.canPopOverlay -> true
            elements.canPop -> true
            else -> false
        }

    private fun Elements<C>.replaceLastWith(replacement: RoutingHistoryElement<C>): Elements<C> =
        toMutableList().apply { set(lastIndex, replacement) }
}

private val <C : Parcelable> Elements<C>.canPopContent: Boolean
    get() = size > 1

internal val <C : Parcelable> Elements<C>.canPop: Boolean
    get() = canPopContent || canPopOverlay

internal val <C : Parcelable> Elements<C>.canPopOverlay: Boolean
    get() = lastOrNull()?.overlays?.isNotEmpty() == true

fun <C : Parcelable> BackStack<C>.pop() {
    accept(Pop())
}
