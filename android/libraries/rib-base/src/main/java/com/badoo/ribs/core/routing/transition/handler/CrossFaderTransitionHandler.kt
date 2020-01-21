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
import com.badoo.ribs.core.routing.transition.invoke


class CrossFaderTransitionHandler<T>(
    private val duration: Long = 300,
    private val interpolator: Interpolator = AccelerateDecelerateInterpolator()
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>) {
        elements { fade(duration, interpolator) }
    }

    fun <T> TransitionElement<out T>.fade(
        duration: Long,
        interpolator: Interpolator = AccelerateDecelerateInterpolator()
    ) {
        progressEvaluator = ProgressEvaluator.InProgress()

        val (from, to) = when (direction) {
            is Exit -> 1f to 0f
            is Enter -> 0f to 1f
        }

        val valueAnimator = ValueAnimator.ofFloat(from, to)
        valueAnimator.interpolator = interpolator
        valueAnimator.duration = duration
        valueAnimator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            this.view.alpha = (progress)
            this.view.invalidate()
            (progressEvaluator as ProgressEvaluator.InProgress).progress = 1.0f * (progress - from) / (to - from)
        }

        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                progressEvaluator = ProgressEvaluator.Finished
            }
        })

        valueAnimator.start()
    }
}

