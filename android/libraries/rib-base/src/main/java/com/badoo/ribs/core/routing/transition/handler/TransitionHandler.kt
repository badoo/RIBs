package com.badoo.ribs.core.routing.transition.handler

import com.badoo.ribs.core.routing.transition.TransitionElement


interface TransitionHandler<C> {

    fun onTransition(elements: List<TransitionElement<out C>>)

    companion object {
        fun <C> multiple(vararg transitionhandlers: TransitionHandler<C>) =
            Multiple(transitionhandlers.toList())
    }

    class Multiple<C>(
        private val transitionhandlers: List<TransitionHandler<C>>
    ) : TransitionHandler<C> {
        private val handlers: List<TransitionHandler<C>>

        init {
            handlers = moveSharedElementToBeginningOfList()
        }

        private fun moveSharedElementToBeginningOfList(): List<TransitionHandler<C>> {
            val list = transitionhandlers.toMutableList()
            val idx = list.indexOfFirst { it is SharedElements<*> }
            if (idx > 0) {
                val removed = list.removeAt(idx)
                list.add(0, removed)
            }
            return list
        }

        override fun onTransition(elements: List<TransitionElement<out C>>) {
            handlers.forEach {
                it.onTransition(elements)
            }
        }
    }
}

