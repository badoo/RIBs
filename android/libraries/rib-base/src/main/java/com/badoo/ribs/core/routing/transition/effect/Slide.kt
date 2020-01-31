package com.badoo.ribs.core.routing.transition.effect

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Point
import android.view.Display
import android.view.animation.Interpolator
import androidx.annotation.CheckResult
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.handler.defaultDuration
import com.badoo.ribs.core.routing.transition.handler.defaultInterpolator
import com.badoo.ribs.core.routing.transition.progress.SingleProgressEvaluator
import kotlin.math.abs

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

    val startProgress = when (_gravity) {
        Gravity.LEFT, Gravity.RIGHT -> view.translationX / (to - from)
        Gravity.TOP, Gravity.BOTTOM -> view.translationY / (to - from)
    }

    val valueAnimator = ValueAnimator.ofFloat(startProgress, 1f)
    val id = System.identityHashCode(valueAnimator)
    valueAnimator.interpolator = interpolator
    valueAnimator.duration = abs((1 - startProgress) * duration).toLong()
    valueAnimator.addUpdateListener { animation ->
        val progress = animation.animatedValue as Float
        update(from + progress * (to - from))
        evaluator.updateProgress(progress)
    }

    valueAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)
            evaluator.start()
        }

        override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
            super.onAnimationEnd(animation, isReverse)
            if (isReverse) {
                evaluator.reset()
            } else {
                evaluator.markFinished()
            }
        }
    })

    return Transition.from(valueAnimator)
}
