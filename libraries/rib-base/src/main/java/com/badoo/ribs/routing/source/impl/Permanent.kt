package com.badoo.ribs.routing.source.impl

import android.os.Parcelable
import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.just
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistory
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.RoutingSource

class Permanent<C : Parcelable>(
    permanents: Iterable<C>
) : RoutingSource<C> {

    private val routingElements =
        permanents.mapIndexed { idx, configuration ->
            RoutingHistoryElement(
                activation = RoutingHistoryElement.Activation.ACTIVE,
                routing = Routing(
                    configuration = configuration,
                    identifier = Routing.Identifier(
                        "Permanent $idx"
                    )
                )
            )
        }

    private val permanentHistory =
        RoutingHistory.from(
            routingElements
        )

    override fun baseLineState(fromRestored: Boolean): RoutingHistory<C> =
        if (fromRestored) {
            RoutingHistory.from(
                routingElements
            )
        } else {
            RoutingHistory.from(
                emptySet()
            )
        }

    override fun remove(identifier: Routing.Identifier) {
        // no-op -- it's permanent!
    }

    override fun observe(callback: (RoutingHistory<C>) -> Unit): Cancellable =
        just { permanentHistory }.observe(callback)
}
