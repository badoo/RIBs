package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.routing.source.backstack.BackStackFeature.Operation
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.BackStack

data class SingleTop<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: BackStack<C>): Boolean =
        true

    override fun invoke(backStack: BackStack<C>): BackStack<C> {
        val targetClass = configuration.javaClass
        val lastIndexOfSameClass = backStack.indexOfLast {
            targetClass.isInstance(it.routing.configuration)
        }

        val operation: (BackStack<C>) -> BackStack<C> =
            if (lastIndexOfSameClass == -1) {
                Push(configuration)
            } else {
                if (backStack[lastIndexOfSameClass].routing.configuration == configuration) {
                    SingleTopReactivateBackStackOperation(lastIndexOfSameClass)
                } else {
                    SingleTopReplaceBackStackOperation(configuration, lastIndexOfSameClass)
                }
            }

        return operation.invoke(backStack)
    }

    private class SingleTopReactivateBackStackOperation<C : Parcelable>(
        private val position: Int
    ) : (BackStack<C>) -> BackStack<C> {

        override fun invoke(backStack: BackStack<C>): BackStack<C> =
            backStack.dropLast(backStack.size - position - 1)
    }

    private class SingleTopReplaceBackStackOperation<C : Parcelable>(
        private val configuration: C,
        private val position: Int
    ) : (BackStack<C>) -> BackStack<C> {

        override fun invoke(backStack: BackStack<C>): BackStack<C> =
            backStack.dropLast(backStack.size - position) + RoutingHistoryElement(
                Routing(
                    configuration
                )
            )
    }
}

fun <C : Parcelable> BackStackFeature<C>.singleTop(configuration: C) {
    accept(Operation(SingleTop(configuration)))
}
