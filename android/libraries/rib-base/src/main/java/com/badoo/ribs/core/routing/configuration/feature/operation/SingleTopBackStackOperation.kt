package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation

data class SingleTopBackStackOperation<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: BackStack<C>): Boolean =
        true

    override fun invoke(backStack: BackStack<C>): BackStack<C> {
        val targetClass = configuration.javaClass
        val lastIndexOfSameClass = backStack.indexOfLast {
            targetClass.isInstance(it.configuration)
        }

        val operation: BackStackOperation<C> =
            if (lastIndexOfSameClass == -1) {
                PushBackStackOperation(configuration)
            } else {
                if (backStack[lastIndexOfSameClass] == configuration) {
                    SingleTopReactivateBackStackOperation(configuration, lastIndexOfSameClass)
                } else {
                    SingleTopReplaceBackStackOperation(configuration, lastIndexOfSameClass)
                }
            }

        return operation(backStack)
    }

    private data class SingleTopReactivateBackStackOperation<C : Parcelable>(
        private val configuration: C,
        private val position: Int
    ) : BackStackOperation<C> {

        override fun isApplicable(backStack: BackStack<C>): Boolean =
            position != -1 && backStack[position] == configuration

        override fun invoke(backStack: BackStack<C>): BackStack<C> =
            backStack.dropLast(backStack.size - position - 1)
    }

    private data class SingleTopReplaceBackStackOperation<C : Parcelable>(
        private val configuration: C,
        private val position: Int
    ) : BackStackOperation<C> {

        override fun isApplicable(backStack: BackStack<C>): Boolean =
            position != -1 && backStack[position] != configuration

        override fun invoke(backStack: BackStack<C>): BackStack<C> =
            backStack.dropLast(backStack.size - position) + BackStackElement(configuration)
    }
}

fun <C : Parcelable> Router<C, *, *, *, *>.singleTop(configuration: C) {
    acceptOperation(SingleTopBackStackOperation(configuration))
}

internal fun <C : Parcelable> SingleTop(configuration: C) =
    Operation(SingleTopBackStackOperation(configuration))
