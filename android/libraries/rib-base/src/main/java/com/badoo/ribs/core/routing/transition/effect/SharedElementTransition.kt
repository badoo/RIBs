@file:SuppressWarnings("LongMethod")
package com.badoo.ribs.core.routing.transition.effect

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.CheckResult
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.handler.defaultDuration
import com.badoo.ribs.core.routing.transition.handler.defaultInterpolator
import com.badoo.ribs.core.routing.transition.progress.SingleProgressEvaluator
import com.badoo.ribs.core.routing.transition.effect.SharedElementTransition.RotationParams

interface SharedElementTransition {
    data class Params(
        val duration: Long = defaultDuration,
        val exitingElement: (View) -> View?,
        val enteringElement: (View) -> View?,
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
    val exit = filter { it.direction == TransitionDirection.Exit }
    val enter = filter { it.direction == TransitionDirection.Enter }
    val transitions: MutableList<SharedElementTransitionInfo<T>> = mutableListOf()

    transitionParams.forEach { transitionParam ->
        var exitingView: View? = null
        var enteringView: View? = null

        val exitElementForId = exit.find {
            exitingView = transitionParam.exitingElement.invoke(it.view)
            exitingView != null
        }

        if (exitElementForId != null) {
            val enteringElementForId = enter.find {
                enteringView = transitionParam.enteringElement.invoke(it.view)
                enteringView != null
            }

            if (enteringElementForId != null) {
                transitions.add(
                    SharedElementTransitionInfo(
                        exitingElement = exitElementForId,
                        exitingView = exitingView!!, // guaranteed by find clause
                        enteringElement = enteringElementForId,
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

            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                super.onAnimationEnd(animation, isReverse)
                rootView.removeView(exitingView)
                if (isReverse) {
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

    return Transition.from(valueAnimator)
}
