package com.badoo.ribs.routing.source

import android.os.Parcelable
import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistory
import com.badoo.ribs.routing.history.RoutingHistoryDiffer
import com.badoo.ribs.routing.state.changeset.TransitionDescriptor
import com.badoo.ribs.routing.state.feature.Transaction
import com.badoo.ribs.util.RIBs

/**
 * Takes two consecutive [RoutingHistory] emissions, and calculates their difference as a set of
 * [RoutingCommand]s.
 *
 * @see [RoutingHistoryDiffer.diff]
 */
internal fun <C : Parcelable> RoutingSource<C>.changes(fromRestored: Boolean): Source<Transaction<C>> =
    object : Source<Transaction<C>> {
        override fun observe(callback: (Transaction<C>) -> Unit): Cancellable {
            var previous = baseLineState(fromRestored)
            return this@changes.observe {
                diffState(previous, it, callback)
                previous = it
            }
        }

        private fun diffState(
            previous: RoutingHistory<C>,
            current: RoutingHistory<C>,
            reportChange: (Transaction<C>) -> Unit
        ) {
            current.ensureUniqueIds()

            val commands =
                RoutingHistoryDiffer.diff(
                    previous,
                    current
                )

            if (commands.isNotEmpty()) {
                reportChange(
                    Transaction.RoutingChange(
                        descriptor = TransitionDescriptor(
                            from = previous,
                            to = current
                        ),
                        changeset = commands.toList() // TODO consider to rename ListOfCommands to SetOfCommands?
                    )
                )
            }
        }
    }

internal fun <C : Parcelable> RoutingHistory<C>.ensureUniqueIds() {
    val ids = mutableSetOf<Routing.Identifier>()
    forEach { element ->
        if (ids.contains(element.routing.identifier)) {
            val errorMessage = "Non-unique content id found: ${element.routing.identifier}"
            RIBs.errorHandler.handleNonFatalError(errorMessage,
                NonUniqueRoutingIdentifierException(
                    errorMessage
                )
            )
        }
        ids.add(element.routing.identifier)

        element.overlays.forEach { overlay ->
            if (ids.contains(overlay.identifier)) {
                val errorMessage = "Non-unique overlay id found: ${overlay.identifier}"
                RIBs.errorHandler.handleNonFatalError(errorMessage,
                    NonUniqueRoutingIdentifierException(
                        errorMessage
                    )
                )
            }
            ids.add(overlay.identifier)
        }
    }
}

class NonUniqueRoutingIdentifierException(message: String?) : RuntimeException(message)
