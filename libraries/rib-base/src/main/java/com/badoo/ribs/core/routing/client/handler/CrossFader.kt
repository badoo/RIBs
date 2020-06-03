package com.badoo.ribs.core.routing.client.handler

import android.view.animation.Interpolator
import com.badoo.ribs.core.routing.client.Transition
import com.badoo.ribs.core.routing.client.TransitionDirection
import com.badoo.ribs.core.routing.client.TransitionElement
import com.badoo.ribs.core.routing.client.TransitionPair
import com.badoo.ribs.core.routing.client.effect.fade.fade
import com.badoo.ribs.core.routing.client.invoke


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

