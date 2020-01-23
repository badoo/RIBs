package com.badoo.ribs.core.routing.transition

import android.animation.ValueAnimator

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

        fun from(valueAnimator: ValueAnimator) = object : Transition {
            override fun end() {
                valueAnimator.end()
            }
        }
    }
}
