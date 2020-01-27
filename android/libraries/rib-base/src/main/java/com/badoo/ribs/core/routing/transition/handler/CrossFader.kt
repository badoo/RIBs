package com.badoo.ribs.core.routing.transition.handler

import android.view.animation.Interpolator
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.effect.fade
import com.badoo.ribs.core.routing.transition.invoke


class CrossFader<T>(
    private val duration: Long = defaultDuration,
    private val interpolator: Interpolator = defaultInterpolator
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>) =
        Transition.multiple(
            elements { fade(duration, interpolator) }
        )

}

