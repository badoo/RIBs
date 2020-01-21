package com.badoo.ribs.core.routing.transition.handler

import com.badoo.ribs.core.routing.transition.TransitionElement


interface TransitionHandler<C> {

    fun onTransition(elements: List<TransitionElement<out C>>)
}
