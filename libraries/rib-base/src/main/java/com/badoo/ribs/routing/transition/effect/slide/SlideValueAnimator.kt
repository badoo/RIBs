@file:SuppressWarnings("LongParameterList")
package com.badoo.ribs.routing.transition.effect.slide

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.view.animation.Interpolator
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.effect.Gravity
import com.badoo.ribs.routing.transition.effect.helper.EndValues
import com.badoo.ribs.routing.transition.effect.helper.ReverseHolder
import com.badoo.ribs.routing.transition.progress.SingleProgressEvaluator
import kotlin.math.abs


internal fun <T> TransitionElement<out T>.slideValueAnimator(
    gravity: Gravity,
    endValues: EndValues,
    interpolator: Interpolator,
    duration: Long,
    evaluator: SingleProgressEvaluator,
    reverseHolder: ReverseHolder
): ValueAnimator {
    val startProgress = when (gravity) {
        Gravity.LEFT, Gravity.RIGHT -> view.translationX / endValues.difference
        Gravity.TOP, Gravity.BOTTOM -> view.translationY / endValues.difference
    }

    val valueAnimator = ValueAnimator.ofFloat(startProgress, 1f)
    valueAnimator.interpolator = interpolator
    valueAnimator.duration = abs((1 - startProgress) * duration).toLong()
    valueAnimator.addUpdateListener(slideUpdateListener(gravity, endValues, evaluator))
    valueAnimator.addListener(slideStartEndNotifier(evaluator, reverseHolder))

    return valueAnimator
}

private  fun <T> TransitionElement<out T>.slideUpdateListener(
    gravity: Gravity,
    endValues: EndValues,
    evaluator: SingleProgressEvaluator
): AnimatorUpdateListener = object : AnimatorUpdateListener {
    val update: (Float) -> Unit = when (gravity) {
        Gravity.LEFT, Gravity.RIGHT -> { v: Float -> view.translationX = v }
        Gravity.TOP, Gravity.BOTTOM -> { v: Float -> view.translationY = v }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val progress = animation.animatedValue as Float
        update(endValues.from + progress * (endValues.to - endValues.from))
        evaluator.updateProgress(progress)
    }
}

private  fun slideStartEndNotifier(
    evaluator: SingleProgressEvaluator,
    reverseHolder: ReverseHolder
): AnimatorListenerAdapter = object : AnimatorListenerAdapter() {

    override fun onAnimationStart(animation: Animator?) {
        super.onAnimationStart(animation)
        evaluator.start()
    }

    override fun onAnimationEnd(animation: Animator?) {
        super.onAnimationEnd(animation)
        if (reverseHolder.isReversing) {
            evaluator.reset()
        } else {
            evaluator.markFinished()
        }
    }
}
