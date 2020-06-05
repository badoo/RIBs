package com.badoo.ribs.routing.transition.effect.fade

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.Interpolator
import androidx.annotation.CheckResult
import com.badoo.ribs.routing.transition.Transition
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.effect.helper.ReverseHolder
import com.badoo.ribs.routing.transition.handler.defaultDuration
import com.badoo.ribs.routing.transition.handler.defaultInterpolator
import com.badoo.ribs.routing.transition.progress.SingleProgressEvaluator
import kotlin.math.abs

@CheckResult
fun <T> TransitionElement<out T>.fade(
    duration: Long = defaultDuration,
    interpolator: Interpolator = defaultInterpolator
) : Transition {

    val evaluator = SingleProgressEvaluator()
    progressEvaluator.add(evaluator)

    val (from, to) = when (direction) {
        TransitionDirection.EXIT -> (if (view.alpha != 1.0f) view.alpha else 1f) to 0f
        TransitionDirection.ENTER -> (if (view.alpha != 1.0f) view.alpha else 0f) to 1f
    }

    val valueAnimator = ValueAnimator.ofFloat(from, to)
    valueAnimator.interpolator = interpolator
    valueAnimator.duration = abs((to - from) * duration).toLong()
    valueAnimator.addUpdateListener { animation ->
        val progress = animation.animatedValue as Float
        this.view.alpha = (progress)
        this.view.invalidate()
        evaluator.updateProgress(1.0f * (progress - from) / (to - from))
    }

    val reverseHolder =
        ReverseHolder()
    valueAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            evaluator.start()
        }

        override fun onAnimationEnd(animation: Animator?) {
            if (reverseHolder.isReversing) {
                evaluator.reset()
            } else {
                evaluator.markFinished()
            }
        }
    })

    return Transition.from(valueAnimator, reverseHolder)
}

