package com.badoo.ribs.core.routing.transition.sharedelement

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.CheckResult
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.Transition

interface SharedElementTransition {
    data class Params(
        val exitingElement: (View) -> View?,
        val enteringElement: (View) -> View?,
        val translateXInterpolator: Interpolator = LinearInterpolator(),
        val translateYInterpolator: Interpolator = LinearInterpolator(),
        val scaleXInterpolator: Interpolator = LinearInterpolator(),
        val scaleYInterpolator: Interpolator = LinearInterpolator()
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
    transitionParams: List<SharedElementTransition.Params>,
    duration: Long
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
            it.transition(duration)
        }
    )
}

@CheckResult
internal fun <T> SharedElementTransitionInfo<T>.transition(
    duration: Long
): Transition {
    // TODO consider supporting multiple progressEvaluators with min() evaluation in TransitionElement
    //  right now this stay commented out as it would just override other transitions
    //  but this also means there has to be at least one other transition in addition to shared element transition
    //  for progress evaluation to work properly
    // exitingElement.progressEvaluator = ProgressEvaluator.InProgress()

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

    val valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        this.duration = duration

        addListener(object : AnimatorListenerAdapter() {
            val rootView = exitingView.rootView as ViewGroup

            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)

                (exitingView.parent as ViewGroup).removeView(exitingView)
                rootView.addView(exitingView)
                enteringView.visibility = View.INVISIBLE
            }
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                // progressEvaluator = ProgressEvaluator.Finished FIXME
                rootView.removeView(exitingView)
                enteringView.visibility = View.VISIBLE
            }
        })

        addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            exitingView.translationX = exitingAbsX + progress.x() * targetXDiff - exitingLayoutParams.leftMargin
            exitingView.translationY = exitingAbsY + progress.y() * targetYDiff - exitingLayoutParams.topMargin
            exitingView.scaleX = 1 + progress.scaleX() * (targetScaleX - 1)
            exitingView.scaleY = 1 + progress.scaleY() * (targetScaleY - 1)
        }
    }

    valueAnimator.start()

    return object : Transition {
        override fun end() {
            valueAnimator.end()
        }
    }
}
