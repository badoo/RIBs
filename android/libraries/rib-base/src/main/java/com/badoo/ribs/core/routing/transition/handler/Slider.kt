package com.badoo.ribs.core.routing.transition.handler

import android.view.animation.Interpolator
import com.badoo.ribs.core.routing.transition.TransitionDirection.Enter
import com.badoo.ribs.core.routing.transition.TransitionDirection.Exit
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.effect.Gravity
import com.badoo.ribs.core.routing.transition.effect.slide
import com.badoo.ribs.core.routing.transition.invoke


open class Slider<T>(
    private val gravity: Gravity = Gravity.LEFT,
    private val duration: Long = defaultDuration,
    private val interpolator: Interpolator = defaultInterpolator,
    private val condition: (TransitionElement<out T>) -> Boolean = { true }
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>): Transition {
        val exit = elements.filter { it.direction == Exit && condition(it) }
        val enter = elements.filter { it.direction == Enter && condition(it)}

        return Transition.multiple(
            exit { slide(gravity, duration, interpolator) },
            enter { slide(gravity.reverse(), duration, interpolator) }
        )

    }
}

