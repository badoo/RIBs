@file:SuppressWarnings("LongMethod")
package com.badoo.ribs.core.routing.transition.effect

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.ViewGroup
import android.view.animation.Interpolator
import androidx.annotation.CheckResult
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.effect.AnimationContainer.FindParentById
import com.badoo.ribs.core.routing.transition.effect.AnimationContainer.Parent
import com.badoo.ribs.core.routing.transition.effect.AnimationContainer.RootView
import com.badoo.ribs.core.routing.transition.handler.defaultDuration
import com.badoo.ribs.core.routing.transition.handler.defaultInterpolator
import com.badoo.ribs.core.routing.transition.progress.SingleProgressEvaluator
import kotlin.math.abs


@CheckResult
fun <T> TransitionElement<out T>.slide(
    gravity: Gravity,
    animationContainer: AnimationContainer = Parent,
    duration: Long = defaultDuration,
    interpolator: Interpolator = defaultInterpolator,
    reverseOnBackStack: Boolean = true
) : Transition {
    val container = when (animationContainer) {
        is RootView -> view.rootView as ViewGroup
        is Parent -> view.parent as ViewGroup
        is FindParentById -> view.findParentById(animationContainer.id)
    }
    requireNotNull(container)

    val location = IntArray(2)
    when (animationContainer) {
        is RootView -> { /* no-op, location stays [0,0] */}
        is Parent,
        is FindParentById -> container.getLocationInWindow(location)
    }

    val containerAbsX = location[0]
    val containerAbsY = location[1]
    view.getLocationInWindow(location)
    val viewAbsX = location[0]
    val viewAbsY = location[1]

    val _gravity = if (reverseOnBackStack && (isBackStackOperation xor (direction == TransitionDirection.EXIT))) gravity.reverse() else gravity

    val evaluator = SingleProgressEvaluator()
    progressEvaluator.add(evaluator)

    val diffToAnimationContainer: Int = when (_gravity) {
        Gravity.LEFT -> viewAbsX - containerAbsX
        Gravity.RIGHT -> containerAbsX + container.width - (viewAbsX + view.width)
        Gravity.TOP -> viewAbsY - containerAbsY
        Gravity.BOTTOM -> containerAbsY + container.height - (viewAbsY + view.height)
    }

    val translation = when (_gravity) {
        Gravity.LEFT -> 0f to -(view.width + diffToAnimationContainer).toFloat()
        Gravity.RIGHT -> 0f to (view.width + diffToAnimationContainer).toFloat()
        Gravity.TOP -> 0f to -(view.height + diffToAnimationContainer).toFloat()
        Gravity.BOTTOM -> 0f to (view.height + diffToAnimationContainer).toFloat()
    }

    val (from, to) = when (direction) {
        TransitionDirection.EXIT -> translation
        TransitionDirection.ENTER -> translation.second to translation.first
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
