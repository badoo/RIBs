package com.badoo.ribs.core.routing.transition.handler

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.badoo.ribs.core.routing.transition.TransitionDirection.Enter
import com.badoo.ribs.core.routing.transition.TransitionDirection.Exit
import android.graphics.Point
import android.view.Display
import com.badoo.ribs.core.routing.transition.ProgressEvaluator
import com.badoo.ribs.core.routing.transition.TransitionElement


abstract class AbstractSliderTransitionHandler<T> : TransitionHandler<T> {

    enum class Gravity {
        LEFT, RIGHT, TOP, BOTTOM;

        fun reverse(): Gravity = when (this) {
            LEFT -> RIGHT
            RIGHT -> LEFT
            TOP -> BOTTOM
            BOTTOM -> TOP
        }
    }

    fun <T> TransitionElement<out T>.slide(
        gravity: Gravity,
        duration: Long,
        interpolator: Interpolator = AccelerateDecelerateInterpolator(),
        reverseOnBackStack: Boolean = true
    ) {
        val display: Display = view.display
        val size = Point()
        display.getSize(size)
        val width: Float = (size.x.toFloat())
        val height: Float = (size.y.toFloat())

        val _gravity = if (reverseOnBackStack && (isBackStackOperation xor (direction == Exit))) gravity.reverse() else gravity

        progressEvaluator =
            ProgressEvaluator.InProgress()

        val translation = when (_gravity) {
            Gravity.LEFT -> 0f to -width
            Gravity.RIGHT -> 0f to width
            Gravity.TOP -> 0f to -height
            Gravity.BOTTOM -> 0f to height
        }

        val (from, to) = when (direction) {
            is Exit -> translation
            is Enter -> translation.second to translation.first
        }

        val update: (Float) -> Unit = when (_gravity) {
            Gravity.LEFT, Gravity.RIGHT -> { v: Float -> view.translationX = v }
            Gravity.TOP, Gravity.BOTTOM -> { v: Float -> view.translationY = v }
        }

        val valueAnimator = ValueAnimator.ofFloat(from, to)
        valueAnimator.interpolator = interpolator
        valueAnimator.duration = duration
        valueAnimator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            update(progress)
            this.view.invalidate()
            (progressEvaluator as ProgressEvaluator.InProgress).progress = 1.0f * (progress - from) / (to - from)
        }

        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                progressEvaluator =
                    ProgressEvaluator.Finished
            }
        })

        valueAnimator.start()
    }
}

