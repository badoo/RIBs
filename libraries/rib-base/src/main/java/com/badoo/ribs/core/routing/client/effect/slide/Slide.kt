@file:SuppressWarnings("LongParameterList")
package com.badoo.ribs.core.routing.client.effect.slide

import android.view.animation.Interpolator
import androidx.annotation.CheckResult
import com.badoo.ribs.core.routing.client.Transition
import com.badoo.ribs.core.routing.client.TransitionDirection
import com.badoo.ribs.core.routing.client.TransitionElement
import com.badoo.ribs.core.routing.client.effect.Gravity
import com.badoo.ribs.core.routing.client.effect.helper.AnimationContainer
import com.badoo.ribs.core.routing.client.effect.helper.AnimationContainer.Parent
import com.badoo.ribs.core.routing.client.effect.helper.ReverseHolder
import com.badoo.ribs.core.routing.client.handler.defaultDuration
import com.badoo.ribs.core.routing.client.handler.defaultInterpolator
import com.badoo.ribs.core.routing.client.progress.SingleProgressEvaluator


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
