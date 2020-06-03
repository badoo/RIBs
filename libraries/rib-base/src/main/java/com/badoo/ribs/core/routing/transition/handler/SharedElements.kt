package com.badoo.ribs.core.routing.transition.handler

import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.TransitionPair
import com.badoo.ribs.core.routing.transition.effect.sharedelement.SharedElementTransition.Params
import com.badoo.ribs.core.routing.transition.effect.sharedelement.sharedElementTransition


open class SharedElements<T>(
    private val params: List<Params>,
    private val condition: (TransitionElement<out T>) -> Boolean = { true }
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>): TransitionPair =
        TransitionPair(
            exiting = null,
            entering = elements
                .filter(condition)
                .sharedElementTransition(params)
        )
}
