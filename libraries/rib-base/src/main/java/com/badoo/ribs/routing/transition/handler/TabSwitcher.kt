package com.badoo.ribs.routing.transition.handler

import android.view.animation.Interpolator
import com.badoo.ribs.routing.transition.Transition
import com.badoo.ribs.routing.transition.TransitionDirection.ENTER
import com.badoo.ribs.routing.transition.TransitionDirection.EXIT
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.TransitionPair
import com.badoo.ribs.routing.transition.effect.Gravity
import com.badoo.ribs.routing.transition.effect.helper.AnimationContainer
import com.badoo.ribs.routing.transition.effect.slide.slide
import com.badoo.ribs.routing.transition.invoke


open class TabSwitcher<T>(
    private val tabsOrder: List<T>,
    private val animationContainer: AnimationContainer = AnimationContainer.Parent,
    private val duration: Long = defaultDuration,
    private val interpolator: Interpolator = defaultInterpolator,
    private val condition: (TransitionElement<out T>) -> Boolean = { true }
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>): TransitionPair {
        val exit = elements.filter { it.direction == EXIT && condition(it) }
        val enter = elements.filter { it.direction == ENTER && condition(it) }

        val exitIndex = tabsOrder.indexOfFirst { it == exit.firstOrNull()?.configuration }
        val enterIndex = tabsOrder.indexOfFirst { it == enter.firstOrNull()?.configuration }
        val dir = enterIndex - exitIndex
        val gravity = if (dir < 0) Gravity.RIGHT else Gravity.LEFT

        return TransitionPair(
            exiting = Transition.multiple(
                exit { slide(gravity, animationContainer, duration, interpolator, false) }
            ),
            entering = Transition.multiple(
                enter { slide(gravity.reverse(), animationContainer, duration, interpolator, false) }
            )

        )
    }
}
