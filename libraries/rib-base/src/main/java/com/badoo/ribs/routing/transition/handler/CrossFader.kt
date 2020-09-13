package com.badoo.ribs.routing.transition.handler

import android.view.animation.Interpolator
import com.badoo.ribs.routing.transition.Transition
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.TransitionPair
import com.badoo.ribs.routing.transition.effect.fade.fade
import com.badoo.ribs.routing.transition.invoke


open class CrossFader<T>(
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

