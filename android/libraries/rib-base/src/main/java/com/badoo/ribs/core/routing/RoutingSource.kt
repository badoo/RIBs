package com.badoo.ribs.core.routing

import android.os.Parcelable
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistory
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.ACTIVE
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer

interface RoutingSource<C : Parcelable> :
    ObservableSource<RoutingHistory<C>>,
    SubtreeBackPressHandler {

    operator fun plus(other: RoutingSource<C>): RoutingSource<C> {
        val merged = Observable.merge(this, other)

        return object : RoutingSource<C>,  ObservableSource<RoutingHistory<C>> by merged{
            override fun remove(identifier: Routing.Identifier) {
                this@RoutingSource.remove(identifier)
                other.remove(identifier)
            }
        }
    }

    fun remove(identifier: Routing.Identifier)

    class Permanent<C : Parcelable>(permanents: Iterable<C>) : RoutingSource<C> {

        companion object {
            fun <C : Parcelable> permanent(permanents: Iterable<C>) = Permanent(permanents)

            fun <C : Parcelable> permanent(vararg permanents: C) = Permanent(permanents.toSet())
        }

        private val routingElements =
            permanents.mapIndexed { idx, configuration ->
                RoutingHistoryElement(
                    activation = ACTIVE,
                    routing = Routing(
                        configuration = configuration,
                        identifier = Routing.Identifier("Permanent $idx")
                    )
            ) }

        private val permanentHistory =
            RoutingHistory.from(routingElements)

        override fun remove(identifier: Routing.Identifier) {
            // no-op -- it's permanent!
        }

        override fun subscribe(observer: Observer<in RoutingHistory<C>>) {
            Observable
                .just(permanentHistory)
                .subscribe(observer)
        }
    }

    class Empty<C : Parcelable> : RoutingSource<C> {
        override fun remove(identifier: Routing.Identifier) {
            // no-op -- it's empty
        }

        override fun subscribe(observer: Observer<in RoutingHistory<C>>) =
            Observable.empty<RoutingHistory<C>>().subscribe(observer)
    }
}
