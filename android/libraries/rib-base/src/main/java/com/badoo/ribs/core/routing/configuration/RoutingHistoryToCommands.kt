package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.RoutingSource
import com.badoo.ribs.core.routing.configuration.feature.TransitionDescriptor
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistory
import com.badoo.ribs.core.routing.history.RoutingHistoryDiffer
import com.badoo.ribs.util.RIBs
import io.reactivex.Observable

/**
 * Takes two consecutive [RoutingHistory] emissions, and calculates their difference as a set of
 * [ConfigurationCommand]s.
 *
 * @see [RoutingHistoryDiffer.diff]
 */
internal fun <C : Parcelable> RoutingSource<C>.toCommands(): Observable<Transaction<C>> =
    Observable.wrap(this)
        .startWith(baseLineState)
        .buffer(2, 1)
        .filter { it.size == 2 } // In case a source has onComplete() before emitting 2 elements
        .flatMap { (previous, current) ->
            current.ensureUniqueIds()

            val commands =
                RoutingHistoryDiffer.diff(
                    previous,
                    current
                )
            when {
                commands.isNotEmpty() -> Observable.just(
                    Transaction.ListOfCommands(
                        descriptor = TransitionDescriptor(
                            from = previous,
                            to = current
                        ),
                        commands = commands.toList() // TODO consider to rename ListOfCommands to SetOfCommands?
                    )
                )
                else -> Observable.empty()
            }
        }

internal fun <C : Parcelable> RoutingHistory<C>.ensureUniqueIds() {
    val ids = mutableSetOf<Routing.Identifier>()
    forEach { element ->
        if (ids.contains(element.routing.identifier)) {
            val errorMessage = "Non-unique content id found: ${element.routing.identifier}"
            RIBs.errorHandler.handleNonFatalError(errorMessage, NonUniqueRoutingIdentifierException(errorMessage))
        }
        ids.add(element.routing.identifier)

        element.overlays.forEach { overlay ->
            if (ids.contains(overlay.identifier)) {
                val errorMessage = "Non-unique overlay id found: ${overlay.identifier}"
                RIBs.errorHandler.handleNonFatalError(errorMessage, NonUniqueRoutingIdentifierException(errorMessage))
            }
            ids.add(overlay.identifier)
        }
    }
}

class NonUniqueRoutingIdentifierException(message: String?) : RuntimeException(message)
