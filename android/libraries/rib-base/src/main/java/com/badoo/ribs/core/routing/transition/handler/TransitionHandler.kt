package com.badoo.ribs.core.routing.transition.handler

import com.badoo.ribs.core.routing.transition.TransitionElement


interface TransitionHandler<C> {

    fun onTransition(elements: List<TransitionElement<out C>>)

    companion object {
        fun <C> multiple(vararg transitionhandlers: TransitionHandler<C>) = object : TransitionHandler<C> {
            override fun onTransition(elements: List<TransitionElement<out C>>) {
                transitionhandlers.forEach {
                    it.onTransition(elements)
                }
            }
        }
    }
}
