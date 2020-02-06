package com.badoo.ribs.core.routing.transition

import android.animation.ValueAnimator

interface Transition {

    fun start()

    fun end()

    fun pause()

    fun reverse()

    companion object {
        fun multiple(vararg transitions: Collection<Transition?>) = object : Transition {
            override fun start() {
                transitions.forEach {
                    it.forEach { it?.start() }
                }
            }

            override fun end() {
                transitions.forEach {
                    it.forEach { it?.end() }
                }
            }

            override fun pause() {
                transitions.forEach {
                    it.forEach { it?.pause() }
                }
            }

            override fun reverse() {
                transitions.forEach {
                    it.forEach { it?.reverse() }
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

            override fun pause() {
                valueAnimator.pause()
            }

            override fun reverse() {
                valueAnimator.reverse()
            }
        }
    }
}
