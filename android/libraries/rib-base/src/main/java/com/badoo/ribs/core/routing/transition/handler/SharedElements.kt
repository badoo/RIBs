package com.badoo.ribs.core.routing.transition.handler

import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.sharedelement.SharedElementTransition.Params
import com.badoo.ribs.core.routing.transition.sharedelement.sharedElementTransition


open class SharedElements<T>(
    private val duration: Long = 300,
    private val params: List<Params>
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>) {
        params.let { elements.sharedElementTransition(params, duration) }
    }
}
