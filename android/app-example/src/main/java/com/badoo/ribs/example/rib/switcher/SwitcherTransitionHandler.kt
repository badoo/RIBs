package com.badoo.ribs.example.rib.switcher

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.badoo.ribs.core.routing.transition.ProgressEvaluator
import com.badoo.ribs.core.routing.transition.TransitionDirection.*
import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.TransitionHandler
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration


class SwitcherTransitionHandler : TransitionHandler<Configuration> {
    override fun onTransition(elements: List<TransitionElement<out Configuration>>) {
        // TODO: simplify by kotlin dsl:
//        val helloWorldExit =
//            elements.find { it.identifier is HelloWorld && it.direction == TransitionDirection.Exit }
//        val helloWorldEnter =
//            elements.find { it.identifier is HelloWorld && it.direction == TransitionDirection.Enter }
//        val fooBarExit =
//            elements.find { it.identifier is FooBar && it.direction == TransitionDirection.Exit }
//        val fooBarEnter =
//            elements.find { it.identifier is FooBar && it.direction == TransitionDirection.Enter }

        val exit = elements.filter { it.direction == Exit }
        val enter = elements.filter { it.direction == Enter }

        exit { slide(0f, 1500f, 300) }
        enter { slide(-1500f, 0f, 300) }

        // or:
//        elements
//            .filter { it.direction == Exit }
//            .slide(0f, 1500f, 300)
//
//        elements
//            .filter { it.direction == Enter }
//            .slide(-1500f, 0f, 300)

//        when {
//            // TODO: simplify by kotlin dsl:
//            helloWorldExit != null -> helloWorldExit.let {
//                300L.let { duration ->
//                    helloWorldExit!! { slide(0f, 1500f, duration) }
//                    (elements - helloWorldExit) { delay(duration) }
//                }
//            }
//
//            helloWorldEnter != null -> {
//                helloWorldEnter!! { slide(-1500f, 0f, 500) }
//            }
//        }
    }

    operator fun <T> TransitionElement<out T>?.invoke(transition: TransitionElement<out T>.() -> Unit) {
        this?.apply(transition)
    }

    operator fun <T> List<TransitionElement<out T>>.invoke(transition: TransitionElement<out T>.() -> Unit) {
        forEach {
            it.apply(transition)
        }
    }

    private fun <T> List<TransitionElement<out T>>.slide(
        from: Float,
        to: Float,
        duration: Long,
        interpolator: Interpolator = AccelerateDecelerateInterpolator()
    ) {
        forEach { it.slide(from, to, duration, interpolator) }
    }

    private fun <T> TransitionElement<out T>.slide(
        from: Float,
        to: Float,
        duration: Long,
        interpolator: Interpolator = AccelerateDecelerateInterpolator()
    ) {
        progressEvaluator = ProgressEvaluator.InProgress()

        val valueAnimator = ValueAnimator.ofFloat(from, to)
        valueAnimator.interpolator = interpolator
        valueAnimator.duration = duration
        valueAnimator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            // TODO could be controlled by Gravity
            this.view.translationX = progress
            this.view.invalidate()
            (progressEvaluator as ProgressEvaluator.InProgress).progress = 1.0f * (progress - from) / (to - from)
            // TODO granular progress update of evaluator
        }

        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                progressEvaluator = ProgressEvaluator.Finished
            }
        })

        valueAnimator.start()
    }

    private fun <T> TransitionElement<out T>.delay(
        duration: Long
    ) {
        progressEvaluator = ProgressEvaluator.InProgress()

        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = duration
        this.view.visibility = View.GONE

        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                this@delay.view.visibility = View.VISIBLE
                progressEvaluator = ProgressEvaluator.Finished
            }
        })

        valueAnimator.start()
    }
}

