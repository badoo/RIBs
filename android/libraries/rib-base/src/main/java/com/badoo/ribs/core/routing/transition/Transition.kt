package com.badoo.ribs.core.routing.transition

interface Transition {

    fun end()

    companion object {
        fun multiple(vararg transitions: Collection<Transition>) = object :
            Transition {
            override fun end() {
                transitions
                    .flatMap {
                        it.map { it.end() }
                    }
            }
        }
    }
}
