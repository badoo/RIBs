package com.badoo.ribs.routing.transition

import android.view.View
import androidx.annotation.CheckResult
import com.badoo.ribs.core.Rib
import com.badoo.ribs.routing.transition.progress.MultiProgressEvaluator
import com.badoo.ribs.routing.transition.progress.ProgressEvaluator

/**
 * @param addedOrRemoved the flag will be true for any [Routing] that either just got created,
 * or just got finally removed. Respectively, the flag will be false for any [Routing] that was already
 * created and just got reactivated from an inactive state, or got deactivated but without final removal.
 * You can use this information to trigger different animation for first time create / final destroy,
 * and temporary off-screen / on-screen transitions.
 */
open class TransitionElement<C>(
    val configuration: C,
    val direction: TransitionDirection,
    val addedOrRemoved: Boolean,
    val identifier: Rib.Identifier,
    val view: View,
    val progressEvaluator: MultiProgressEvaluator = MultiProgressEvaluator()
) : ProgressEvaluator by progressEvaluator


@CheckResult
operator fun <T> List<TransitionElement<out T>>.invoke(transition: TransitionElement<out T>.() -> Transition): List<Transition> =
    map {
        transition.invoke(it)
    }
