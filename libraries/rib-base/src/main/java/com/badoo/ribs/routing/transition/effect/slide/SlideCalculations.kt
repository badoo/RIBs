package com.badoo.ribs.routing.transition.effect.slide

import android.view.ViewGroup
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.effect.Gravity
import com.badoo.ribs.routing.transition.effect.helper.AnimationContainer
import com.badoo.ribs.routing.transition.effect.helper.AnimationContainer.FindParentById
import com.badoo.ribs.routing.transition.effect.helper.AnimationContainer.Parent
import com.badoo.ribs.routing.transition.effect.helper.AnimationContainer.RootView
import com.badoo.ribs.routing.transition.effect.helper.EndValues
import com.badoo.ribs.routing.transition.effect.helper.findParentById


internal fun <T> TransitionElement<out T>.slideEndValues(
    animationContainer: AnimationContainer,
    gravity: Gravity
): EndValues {
    val container = when (animationContainer) {
        is RootView -> view.rootView as ViewGroup
        is Parent -> view.parent as ViewGroup
        is FindParentById -> view.findParentById(animationContainer.id)
    }

    requireNotNull(container)

    val diffToAnimationContainer: Int = diffToAnimationContainer(animationContainer, gravity, container)

    return when (gravity) {
        Gravity.LEFT -> EndValues(0f, -(view.width + diffToAnimationContainer).toFloat())
        Gravity.RIGHT -> EndValues(0f, (view.width + diffToAnimationContainer).toFloat())
        Gravity.TOP -> EndValues(0f, -(view.height + diffToAnimationContainer).toFloat())
        Gravity.BOTTOM -> EndValues(0f, (view.height + diffToAnimationContainer).toFloat())
    }
}

private fun <T> TransitionElement<out T>.diffToAnimationContainer(
    animationContainer: AnimationContainer,
    gravity: Gravity,
    container: ViewGroup
): Int {
    val location = IntArray(2)
    when (animationContainer) {
        is RootView -> { /* no-op, location stays [0,0] */ }
        is Parent,
        is FindParentById -> container.getLocationInWindow(location)
    }

    val containerAbsX = location[0]
    val containerAbsY = location[1]
    view.getLocationInWindow(location)
    val viewAbsX = location[0]
    val viewAbsY = location[1]

    return when (gravity) {
        Gravity.LEFT -> viewAbsX - containerAbsX
        Gravity.RIGHT -> containerAbsX + container.width - (viewAbsX + view.width)
        Gravity.TOP -> viewAbsY - containerAbsY
        Gravity.BOTTOM -> containerAbsY + container.height - (viewAbsY + view.height)
    }
}
