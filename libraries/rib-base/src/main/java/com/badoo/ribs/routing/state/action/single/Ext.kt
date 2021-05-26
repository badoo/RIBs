package com.badoo.ribs.routing.state.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement

internal fun <C : Parcelable> Node<*>.createTransitionElements(
    item: RoutingContext.Resolved<C>,
    direction: TransitionDirection,
    addedOrRemoved: Boolean
): List<TransitionElement<C>> {
    val elements = mutableListOf<TransitionElement<C>>()

    if (isViewless) {
        children.forEach {
            elements += it.createTransitionElements(
                item, direction, addedOrRemoved
            )
        }
    } else {
        val ribView = view
        requireNotNull(ribView)
        elements += TransitionElement(
            configuration = item.routing.configuration, // TODO consider passing the whole RoutingElement
            direction = direction,
            addedOrRemoved = addedOrRemoved,
            identifier = identifier,
            view = ribView.androidView
        )
    }

    return elements
}
