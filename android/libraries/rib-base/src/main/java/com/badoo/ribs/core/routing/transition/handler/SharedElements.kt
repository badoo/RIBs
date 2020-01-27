package com.badoo.ribs.core.routing.transition.handler

import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.sharedelement.SharedElementTransition.Params
import com.badoo.ribs.core.routing.transition.sharedelement.sharedElementTransition


open class SharedElements<T>(
    private val params: List<Params>,
    private val condition: (TransitionElement<out T>) -> Boolean = { true }
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>): Transition =
        elements
            .filter(condition)
            .sharedElementTransition(params)
}
