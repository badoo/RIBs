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
import io.reactivex.functions.BiFunction

interface RoutingSource<C : Parcelable> :
    ObservableSource<RoutingHistory<C>>,
    SubtreeBackPressHandler {

    operator fun plus(other: RoutingSource<C>): RoutingSource<C> = Combined(this, other)

    fun remove(identifier: Routing.Identifier)

    data class Combined<C : Parcelable>(
        val first: RoutingSource<C>,
        val second: RoutingSource<C>
    ) : RoutingSource<C> {

        data class CombinedHistory<C : Parcelable>(
            val first: RoutingHistory<C>,
            val second: RoutingHistory<C>
        ):  RoutingHistory<C> {

            override fun iterator(): Iterator<RoutingHistoryElement<C>> =
                ConcatIterator(first.iterator()) + second.iterator()
        }

        private val combined = Observable.combineLatest(
            first,
            second,
            BiFunction<RoutingHistory<C>, RoutingHistory<C>, RoutingHistory<C>> { source1, source2 ->
                CombinedHistory(source1, source2)
            }
        )

        override fun subscribe(observer: Observer<in RoutingHistory<C>>) {
            combined.subscribe(observer)
        }

        override fun remove(identifier: Routing.Identifier) {
            first.remove(identifier)
            second.remove(identifier)
        }

        override fun handleBackPressFirst(): Boolean =
            first.handleBackPressFirst() || second.handleBackPressFirst()

        override fun handleBackPressFallback(): Boolean =
            first.handleBackPressFallback() || second.handleBackPressFallback()
    }

    // TODO extract
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

    // TODO extract
    class Empty<C : Parcelable> : RoutingSource<C> {
        override fun remove(identifier: Routing.Identifier) {
            // no-op -- it's empty
        }

        override fun subscribe(observer: Observer<in RoutingHistory<C>>) =
            Observable.empty<RoutingHistory<C>>().subscribe(observer)
    }
}

