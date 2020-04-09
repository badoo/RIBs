package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
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
            backStack.canPop -> true
            else -> false
        }

    private fun List<BackStackElement<C>>.replaceLastWith(replacement: BackStackElement<C>): List<BackStackElement<C>> =
        toMutableList().apply { set(lastIndex, replacement) }
}

private val <C : Parcelable> List<BackStackElement<C>>.canPopContent: Boolean
    get() = size > 1

internal val <C : Parcelable> List<BackStackElement<C>>.canPop: Boolean
    get() = canPopContent || canPopOverlay

internal val <C : Parcelable> List<BackStackElement<C>>.canPopOverlay: Boolean
    get() = lastOrNull()?.overlays?.isNotEmpty() == true

internal fun <C : Parcelable> Pop() =
    ExtendedOperation(PopBackStackOperation<C>())
