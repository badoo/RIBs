package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation.ExtendedOperation

class PopBackStackOperation<C : Parcelable> : BackStackOperation<C> {

    override fun modifyStack(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> =
        when {
            backStack.canPopOverlay ->
                backStack.replaceLastWith(
                    backStack.last().copy(
                        overlays = backStack.last().overlays.dropLast(1)
                    )
                )
            backStack.canPopContent -> backStack.dropLast(1)
            else -> backStack
        }

    override fun isApplicable(backStack: List<BackStackElement<C>>): Boolean =
        when {
            backStack.canPopOverlay -> true
            backStack.canPopContent -> true
            else -> false
        }

    private val List<BackStackElement<C>>.canPopOverlay: Boolean
        get() = lastOrNull()?.overlays?.isNotEmpty() == true

    private val List<BackStackElement<C>>.canPopContent: Boolean
        get() = size > 1

    private fun List<BackStackElement<C>>.replaceLastWith(replacement: BackStackElement<C>): List<BackStackElement<C>> =
        toMutableList().apply { set(lastIndex, replacement) }
}

fun <C : Parcelable> Router<C, *, *, *, *>.popBackStack(): Boolean {
    var result = false
    acceptBackStack {
        result = if (state.canPop) {
            accept(Pop())
            true
        } else {
            false
        }
    }

    return result
}

fun <C : Parcelable> Router<C, *, *, *, *>.popOverlay(): Boolean {
    var result = false
    acceptBackStack {
        result = if (state.canPopOverlay) {
            accept(Pop())
            true
        } else {
            false
        }
    }

    return result
}

internal fun <C : Parcelable> Pop() =
    ExtendedOperation(PopBackStackOperation<C>())
