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


open class Slider<T>(
    private val gravity: Gravity = Gravity.LEFT,
    private val animationContainer: AnimationContainer = AnimationContainer.RootView,
    private val duration: Long = defaultDuration,
    private val interpolator: Interpolator = defaultInterpolator,
    private val condition: (TransitionElement<out T>) -> Boolean = { true }
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>): TransitionPair {
        val exit = elements.filter { it.direction == EXIT && condition(it) }
        val enter = elements.filter { it.direction == ENTER && condition(it)}

        return TransitionPair(
            exiting = Transition.multiple(
                exit { slide(gravity, animationContainer, duration, interpolator) }
            ),
            entering = Transition.multiple(
                enter { slide(gravity.reverse(), animationContainer, duration, interpolator) }
            )
        )
    }
}

