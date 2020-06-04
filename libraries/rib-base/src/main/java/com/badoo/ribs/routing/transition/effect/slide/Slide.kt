@file:SuppressWarnings("LongParameterList")
package com.badoo.ribs.routing.transition.effect.slide

import android.view.animation.Interpolator
import androidx.annotation.CheckResult
import com.badoo.ribs.routing.transition.Transition
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.effect.Gravity
import com.badoo.ribs.routing.transition.effect.helper.AnimationContainer
import com.badoo.ribs.routing.transition.effect.helper.AnimationContainer.Parent
import com.badoo.ribs.routing.transition.effect.helper.ReverseHolder
import com.badoo.ribs.routing.transition.handler.defaultDuration
import com.badoo.ribs.routing.transition.handler.defaultInterpolator
import com.badoo.ribs.routing.transition.progress.SingleProgressEvaluator


@CheckResult
fun <T> TransitionElement<out T>.slide(
    gravity: Gravity,
    animationContainer: AnimationContainer = Parent,
    duration: Long = defaultDuration,
    interpolator: Interpolator = defaultInterpolator,
    reverseWhenAddedOrRemoved: Boolean = true
) : Transition {
    val evaluator = SingleProgressEvaluator().also { progressEvaluator.add(it) }
    val actualGravity = if (reverseWhenAddedOrRemoved && (addedOrRemoved xor (direction == TransitionDirection.ENTER))) gravity.reverse() else gravity
    val endValues = slideEndValues(animationContainer, actualGravity).let {
        when (direction) {
            TransitionDirection.EXIT -> it
            TransitionDirection.ENTER -> it.reverse()
        }
    }
    val reverseHolder =
        ReverseHolder()

    return Transition.from(
        slideValueAnimator(actualGravity, endValues, interpolator, duration, evaluator, reverseHolder),
        reverseHolder
    )
}
