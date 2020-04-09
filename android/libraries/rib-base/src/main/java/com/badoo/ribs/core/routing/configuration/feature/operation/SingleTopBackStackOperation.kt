package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature

data class SingleTopBackStackOperation<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: List<BackStackElement<C>>): Boolean =
        true

    override fun modifyStack(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> {
        val targetClass = configuration.javaClass
        val lastIndexOfSameClass = backStack.indexOfLast {
            targetClass.isInstance(it.configuration)
        }

        val operation: BackStackOperation<C> = if (lastIndexOfSameClass == -1) {
            PushBackStackOperation(configuration)
        } else {
            if (backStack[lastIndexOfSameClass] == configuration) {
                SingleTopReactivateBackStackOperation(configuration, lastIndexOfSameClass)
            } else {
                SingleTopReplaceBackStackOperation(configuration, lastIndexOfSameClass)
            }
        }

        return operation.modifyStack(backStack)
    }
}

fun <C : Parcelable> Router<C, *, *, *, *>.singleTop(configuration: C) {
    acceptOperation(SingleTopBackStackOperation(configuration))
}

internal fun <C : Parcelable> SingleTop(configuration: C) =
    BackStackFeature.Operation.ExtendedOperation(SingleTopBackStackOperation(configuration))
