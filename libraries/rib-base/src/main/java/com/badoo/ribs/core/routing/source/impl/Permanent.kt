package com.badoo.ribs.core.routing.source.impl

import android.os.Parcelable
import com.badoo.ribs.core.routing.Routing
import com.badoo.ribs.core.routing.history.RoutingHistory
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import com.badoo.ribs.core.routing.source.RoutingSource
import io.reactivex.Observable
import io.reactivex.Observer

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
        Observable.just(
            RoutingHistory.from(
                routingElements
            )
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

    override fun subscribe(observer: Observer<in RoutingHistory<C>>) {
        permanentHistory
            .subscribe(observer)
    }
}
