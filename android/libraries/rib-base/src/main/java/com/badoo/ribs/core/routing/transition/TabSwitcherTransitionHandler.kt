package com.badoo.ribs.core.routing.transition

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.badoo.ribs.core.routing.transition.TransitionDirection.Enter
import com.badoo.ribs.core.routing.transition.TransitionDirection.Exit
import android.graphics.Point
import android.view.Display


open class TabSwitcherTransitionHandler<T>(
    private val tabsOrder: List<T>,
    private val duration: Long = 200,
    private val interpolator: Interpolator = AccelerateDecelerateInterpolator()
) : TransitionHandler<T> {

    override fun onTransition(elements: List<TransitionElement<out T>>) {
        val exit = elements.filter { it.direction == Exit }
        val enter = elements.filter { it.direction == Enter }

        val exitIndex = tabsOrder.indexOfFirst { it == exit.firstOrNull()?.configuration }
        val enterIndex = tabsOrder.indexOfFirst { it == enter.firstOrNull()?.configuration }
        val dir = enterIndex - exitIndex
        val gravity = if (dir < 0) Gravity.RIGHT else Gravity.LEFT

        exit { slide(gravity, duration, interpolator) }
        enter { slide(gravity.reverse(), duration, interpolator) }
    }

    operator fun <T> TransitionElement<out T>?.invoke(transition: TransitionElement<out T>.() -> Unit) {
        this?.apply(transition)
    }

    operator fun <T> List<TransitionElement<out T>>.invoke(transition: TransitionElement<out T>.() -> Unit) {
        forEach {
            it.apply(transition)
        }
    }

    enum class Gravity {
        LEFT, RIGHT, TOP, BOTTOM;

        fun reverse(): Gravity = when (this) {
            LEFT -> RIGHT
            RIGHT -> LEFT
            TOP -> BOTTOM
            BOTTOM -> TOP
        }
    }

    private fun <T> TransitionElement<out T>.slide(
        gravity: Gravity,
        duration: Long,
        interpolator: Interpolator = AccelerateDecelerateInterpolator()
    ) {
        val display: Display = view.display
        val size = Point()
        display.getSize(size)
        val width: Float = (size.x.toFloat())
        val height: Float = (size.y.toFloat())

        progressEvaluator = ProgressEvaluator.InProgress()

        val translation = when (gravity) {
            Gravity.LEFT -> 0f to -width
            Gravity.RIGHT -> 0f to width
            Gravity.TOP -> 0f to -height
            Gravity.BOTTOM -> 0f to height
        }

        val (from, to) = when (direction) {
            is Exit -> translation
            is Enter -> translation.second to translation.first
        }

        val update: (Float) -> Unit = when (gravity) {
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
            (progressEvaluator as ProgressEvaluator.InProgress).progress = 1.0f * (progress - from) / (to - from)
        }

        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                progressEvaluator = ProgressEvaluator.Finished
            }
        })

        valueAnimator.start()
    }
}

