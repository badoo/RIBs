package com.badoo.ribs.core.routing.transition

import android.animation.ValueAnimator

interface Transition {

    fun start()

    fun end()

    companion object {
        fun multiple(vararg transitions: Collection<Transition>) = object : Transition {
            override fun start() {
                transitions.flatMap {
                    it.map { it.start() }
                }
            }

            override fun end() {
                transitions.flatMap {
                    it.map { it.end() }
                }
            }
        }

        fun from(valueAnimator: ValueAnimator) = object : Transition {
            override fun start() {
                valueAnimator.start()
            }

            override fun end() {
                valueAnimator.end()
            }
        }
    }
}
