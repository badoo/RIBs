package com.badoo.ribs.core.routing.transition.effect

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Point
import android.view.Display
import android.view.animation.Interpolator
import androidx.annotation.CheckResult
import com.badoo.ribs.core.routing.transition.SingleProgressEvaluator
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.handler.defaultDuration
import com.badoo.ribs.core.routing.transition.handler.defaultInterpolator

@CheckResult
fun <T> TransitionElement<out T>.slide(
    gravity: Gravity,
    duration: Long = defaultDuration,
    interpolator: Interpolator = defaultInterpolator,
    reverseOnBackStack: Boolean = true
) : Transition {
    val display: Display = view.display
    val size = Point()
    display.getSize(size)
    val width: Float = (size.x.toFloat())
    val height: Float = (size.y.toFloat())

    val _gravity = if (reverseOnBackStack && (isBackStackOperation xor (direction == TransitionDirection.Exit))) gravity.reverse() else gravity

    val evaluator = SingleProgressEvaluator()
    progressEvaluator.add(evaluator)

    val translation = when (_gravity) {
        Gravity.LEFT -> 0f to -width
        Gravity.RIGHT -> 0f to width
        Gravity.TOP -> 0f to -height
        Gravity.BOTTOM -> 0f to height
    }

    val (from, to) = when (direction) {
        is TransitionDirection.Exit -> translation
        is TransitionDirection.Enter -> translation.second to translation.first
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
        evaluator.updateProgress(1.0f * (progress - from) / (to - from))
    }

    valueAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            evaluator.markFinished()
        }
    })

    return Transition.from(valueAnimator)
}
