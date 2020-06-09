package com.badoo.ribs.routing.transition

import android.animation.ValueAnimator
import com.badoo.ribs.routing.transition.effect.helper.ReverseHolder

/**
 * Interfaces between internal routing changes and client code animations.
 */
interface Transition {

    fun start()

    fun end()
    
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

            override fun reverse() {
                transitions.forEach {
                    it.forEach { it?.reverse() }
                }
            }
        }

        fun from(valueAnimator: ValueAnimator, reverseHolder: ReverseHolder = ReverseHolder()) =
            object : Transition {
                override fun start() {
                    valueAnimator.start()
                }

                override fun end() {
                    valueAnimator.end()
                }

                override fun reverse() {
                    valueAnimator.reverse()
                    reverseHolder.isReversing = !reverseHolder.isReversing
                }
            }
    }
}
