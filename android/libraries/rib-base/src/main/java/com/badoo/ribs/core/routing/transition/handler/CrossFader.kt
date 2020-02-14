package com.badoo.ribs.core.routing.transition.handler

import android.view.animation.Interpolator
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.TransitionPair
import com.badoo.ribs.core.routing.transition.effect.fade
import com.badoo.ribs.core.routing.transition.invoke


class CrossFader<T>(
    private val duration: Long = defaultDuration,
    private val interpolator: Interpolator = defaultInterpolator,
    private val condition: (TransitionElement<out T>) -> Boolean = { true }
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>): TransitionPair {
        val exiting = elements
            .filter { it.direction == TransitionDirection.EXIT && condition(it)}
            .invoke { fade(duration, interpolator) }

        val entering = elements
            .filter { it.direction == TransitionDirection.ENTER && condition(it)}
            .invoke { fade(duration, interpolator) }

        return TransitionPair(
            exiting = Transition.multiple(exiting),
            entering = Transition.multiple(entering)
        )
    }
}

