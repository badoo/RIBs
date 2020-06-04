@file:SuppressWarnings("LongMethod")
package com.badoo.ribs.routing.transition.effect.sharedelement

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.CheckResult
import com.badoo.ribs.routing.transition.Transition
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement
import com.badoo.ribs.routing.transition.effect.sharedelement.SharedElementTransition.RotationParams
import com.badoo.ribs.routing.transition.effect.helper.ReverseHolder
import com.badoo.ribs.routing.transition.handler.defaultDuration
import com.badoo.ribs.routing.transition.handler.defaultInterpolator
import com.badoo.ribs.routing.transition.progress.SingleProgressEvaluator

interface SharedElementTransition {
    data class Params(
        val duration: Long = defaultDuration,
        val findExitingElement: (View) -> View?,
        val findEnteringElement: (View) -> View?,
        val translateXInterpolator: Interpolator = LinearInterpolator(),
        val translateYInterpolator: Interpolator = LinearInterpolator(),
        val scaleXInterpolator: Interpolator = LinearInterpolator(),
        val scaleYInterpolator: Interpolator = LinearInterpolator(),
        val rotation: RotationParams? = null,
        val rotationX: RotationParams? = null,
        val rotationY: RotationParams? = null
    )

    data class RotationParams(
        val degrees: Float,
        val interpolator: Interpolator = defaultInterpolator
    )
}

internal data class SharedElementTransitionInfo<T>(
    val exitingElement: TransitionElement<out T>,
    val exitingView: View,
    val enteringElement: TransitionElement<out T>,
    val enteringView: View,
    val params: SharedElementTransition.Params
)

@CheckResult
fun <T> List<TransitionElement<out T>>.sharedElementTransition(
    transitionParams: List<SharedElementTransition.Params>
): Transition {
    val exit = filter { it.direction == TransitionDirection.EXIT }
    val enter = filter { it.direction == TransitionDirection.ENTER }
    val transitions: MutableList<SharedElementTransitionInfo<T>> = mutableListOf()

    transitionParams.forEach { transitionParam ->
        var exitingView: View? = null
        var enteringView: View? = null

        val exitingElement = exit.find {
            exitingView = transitionParam.findExitingElement.invoke(it.view)
            exitingView != null
        }

        if (exitingElement != null) {
            val enteringElement = enter.find {
                enteringView = transitionParam.findEnteringElement.invoke(it.view)
                enteringView != null
            }

            if (enteringElement != null) {
                transitions.add(
                    SharedElementTransitionInfo(
                        exitingElement = exitingElement,
                        exitingView = exitingView!!, // guaranteed by find clause
                        enteringElement = enteringElement,
                        enteringView = enteringView!!, // guaranteed by find clause
                        params = transitionParam
                    )
                )
            }
        }
    }

    return Transition.multiple(
        transitions.map {
            it.transition()
        }
    )
}

@CheckResult
internal fun <T> SharedElementTransitionInfo<T>.transition(): Transition {
    val evaluator = SingleProgressEvaluator()
    exitingElement.progressEvaluator.add(evaluator)
    enteringElement.progressEvaluator.add(evaluator)

    enteringElement.view.measure(0, 0)

    val location = IntArray(2)
    enteringView.getLocationInWindow(location)
    val enteringAbsX = location[0]
    val enteringAbsY = location[1]
    exitingView.getLocationInWindow(location)
    val exitingAbsX = location[0]
    val exitingAbsY = location[1]
    val exitingLayoutParams = (exitingView.layoutParams as ViewGroup.MarginLayoutParams)

    val targetScaleX = 1.0f * enteringView.measuredWidth / exitingView.width
    val targetScaleY = 1.0f * enteringView.measuredHeight / exitingView.height
    val wDiff = (enteringView.measuredWidth - exitingView.width)
    val hDiff = (enteringView.measuredHeight - exitingView.height)
    val targetXDiff = enteringAbsX - exitingAbsX + wDiff / 2f
    val targetYDiff = enteringAbsY - exitingAbsY + hDiff / 2f

    fun Float.x(): Float = params.translateXInterpolator.getInterpolation(this)
    fun Float.y(): Float = params.translateYInterpolator.getInterpolation(this)
    fun Float.scaleX(): Float = params.scaleXInterpolator.getInterpolation(this)
    fun Float.scaleY(): Float = params.scaleYInterpolator.getInterpolation(this)
    fun RotationParams.rotation(progress: Float): Float = degrees * interpolator.getInterpolation(progress)

    val reverseHolder =
        ReverseHolder()
    val valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = params.duration

        addListener(object : AnimatorListenerAdapter() {
            val originalParent = exitingView.parent as ViewGroup
            val originalParentIdx = originalParent.indexOfChild(exitingView)
            val originalLayoutParams = exitingView.layoutParams
            val originalTX = exitingView.translationX
            val originalTY = exitingView.translationY
            val rootView = exitingView.rootView as ViewGroup

            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                evaluator.start()
                originalParent.removeView(exitingView)
                rootView.addView(exitingView)
                enteringView.visibility = View.INVISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                rootView.removeView(exitingView)
                if (reverseHolder.isReversing) {
                    originalParent.addView(exitingView, originalParentIdx, originalLayoutParams)
                    exitingView.translationX = originalTX
                    exitingView.translationY = originalTY
                    evaluator.reset()
                } else {
                    enteringView.visibility = View.VISIBLE
                    evaluator.markFinished()
                }
            }
        })

        addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            exitingView.translationX = exitingAbsX + progress.x() * targetXDiff - exitingLayoutParams.leftMargin
            exitingView.translationY = exitingAbsY + progress.y() * targetYDiff - exitingLayoutParams.topMargin
            exitingView.scaleX = 1 + progress.scaleX() * (targetScaleX - 1)
            exitingView.scaleY = 1 + progress.scaleY() * (targetScaleY - 1)
            params.rotation?.let { exitingView.rotation = it.rotation(progress) }
            params.rotationX?.let { exitingView.rotationX = it.rotation(progress) }
            params.rotationY?.let { exitingView.rotationY = it.rotation(progress) }
        }
    }

    return Transition.from(valueAnimator, reverseHolder)
}
