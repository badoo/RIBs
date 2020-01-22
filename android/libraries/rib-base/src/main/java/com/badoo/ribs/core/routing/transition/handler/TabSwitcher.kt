package com.badoo.ribs.core.routing.transition.handler

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.badoo.ribs.core.routing.transition.TransitionDirection.Enter
import com.badoo.ribs.core.routing.transition.TransitionDirection.Exit
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.effect.Gravity
import com.badoo.ribs.core.routing.transition.effect.slide
import com.badoo.ribs.core.routing.transition.invoke
import com.badoo.ribs.core.routing.transition.sharedelement.SharedElementTransition.Params
import com.badoo.ribs.core.routing.transition.sharedelement.sharedElementTransition


open class TabSwitcher<T>(
    private val tabsOrder: List<T>,
    private val duration: Long = 1000,
    private val interpolator: Interpolator = AccelerateDecelerateInterpolator(),
    private val sharedelementTransitions: List<Params>? = null
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>) {
        val exit = elements.filter { it.direction == Exit }
        val enter = elements.filter { it.direction == Enter }

        val exitIndex = tabsOrder.indexOfFirst { it == exit.firstOrNull()?.configuration }
        val enterIndex = tabsOrder.indexOfFirst { it == enter.firstOrNull()?.configuration }
        val dir = enterIndex - exitIndex
        val gravity = if (dir < 0) Gravity.RIGHT else Gravity.LEFT

        sharedelementTransitions?.let { elements.sharedElementTransition(sharedelementTransitions, duration) }
        exit { slide(gravity, duration, interpolator, false) }
        enter { slide(gravity.reverse(), duration, interpolator, false) }
    }
}
