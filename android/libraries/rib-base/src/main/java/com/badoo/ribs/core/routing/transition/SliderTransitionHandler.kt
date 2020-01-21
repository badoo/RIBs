package com.badoo.ribs.core.routing.transition

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.badoo.ribs.core.routing.transition.TransitionDirection.Enter
import com.badoo.ribs.core.routing.transition.TransitionDirection.Exit


open class SliderTransitionHandler<T>(
    private val gravity: Gravity = Gravity.LEFT,
    private val duration: Long = 200,
    private val interpolator: Interpolator = AccelerateDecelerateInterpolator()
) : AbstractSliderTransitionHandler<T>() {

    override fun onTransition(elements: List<TransitionElement<out T>>) {
        val exit = elements.filter { it.direction == Exit }
        val enter = elements.filter { it.direction == Enter }

        exit { slide(gravity, duration, interpolator) }
        enter { slide(gravity.reverse(), duration, interpolator) }
    }
}

