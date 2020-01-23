package com.badoo.ribs.core.routing.transition.effect

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.CheckResult
import com.badoo.ribs.core.routing.transition.SingleProgressEvaluator
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement

@CheckResult
fun <T> TransitionElement<out T>.fade(
    duration: Long,
    interpolator: Interpolator = AccelerateDecelerateInterpolator()
) : Transition {

    val evaluator = SingleProgressEvaluator()
    progressEvaluator.add(evaluator)

    val (from, to) = when (direction) {
        is TransitionDirection.Exit -> 1f to 0f
        is TransitionDirection.Enter -> 0f to 1f
    }

    val valueAnimator = ValueAnimator.ofFloat(from, to)
    valueAnimator.interpolator = interpolator
    valueAnimator.duration = duration
    valueAnimator.addUpdateListener { animation ->
        val progress = animation.animatedValue as Float
        this.view.alpha = (progress)
        this.view.invalidate()
        evaluator.updateProgress(1.0f * (progress - from) / (to - from))
    }

    valueAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            evaluator.markFinished()
        }
    })

    valueAnimator.start()

    return object : Transition {
        override fun end() {
            valueAnimator.end()
        }
    }
}
