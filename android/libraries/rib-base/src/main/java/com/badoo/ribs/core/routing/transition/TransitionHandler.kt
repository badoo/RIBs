package com.badoo.ribs.core.routing.transition


interface TransitionHandler<C> {

    fun onTransition(elements: List<TransitionElement<out C>>)
}
