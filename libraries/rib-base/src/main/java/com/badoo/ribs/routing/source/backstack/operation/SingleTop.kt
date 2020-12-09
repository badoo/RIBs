package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.Elements

/**
 * Operation:
 *
 * [A, B, C, D] + SingleTop(B) = [A, B]          // of same type and equals, acts as n * Pop
 * [A, B, C, D] + SingleTop(B') = [A, B']        // of same type but not equals, acts as n * Pop + Replace
 * [A, B, C, D] + SingleTop(E) = [A, B, C, D, E] // not found, acts as Push
 */
data class SingleTop<C : Parcelable>(
    private val configuration: C
) : BackStack.Operation<C> {

    override fun isApplicable(elements: Elements<C>): Boolean =
        true

    override fun invoke(elements: Elements<C>): Elements<C> {
        val targetClass = configuration.javaClass
        val lastIndexOfSameClass = elements.indexOfLast {
            targetClass.isInstance(it.routing.configuration)
        }

        val operation: (Elements<C>) -> Elements<C> =
            if (lastIndexOfSameClass == -1) {
                Push(configuration)
            } else {
                if (elements[lastIndexOfSameClass].routing.configuration == configuration) {
                    SingleTopReactivateBackStackOperation(lastIndexOfSameClass)
                } else {
                    SingleTopReplaceBackStackOperation(configuration, lastIndexOfSameClass)
                }
            }

        return operation.invoke(elements)
    }

    private class SingleTopReactivateBackStackOperation<C : Parcelable>(
        private val position: Int
    ) : (Elements<C>) -> Elements<C> {

        override fun invoke(elements: Elements<C>): Elements<C> =
            elements.dropLast(elements.size - position - 1)
    }

    private class SingleTopReplaceBackStackOperation<C : Parcelable>(
        private val configuration: C,
        private val position: Int
    ) : (Elements<C>) -> Elements<C> {

        override fun invoke(elements: Elements<C>): Elements<C> =
            elements.dropLast(elements.size - position) + RoutingHistoryElement(
                Routing(
                    configuration
                )
            )
    }
}

fun <C : Parcelable> BackStack<C>.singleTop(configuration: C) {
    accept(SingleTop(configuration))
}
