package com.badoo.ribs.core.routing

import android.os.Parcelable
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.Routing.Identifier
import com.badoo.ribs.core.routing.history.RoutingHistory
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.ACTIVE
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.INACTIVE
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.BiFunction

interface RoutingSource<C : Parcelable> :
    ObservableSource<RoutingHistory<C>>,
    SubtreeBackPressHandler {

    operator fun plus(other: RoutingSource<C>): RoutingSource<C> = Combined(this, other)

    fun remove(identifier: Identifier)

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

        override fun remove(identifier: Identifier) {
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
                        identifier = Identifier("Permanent $idx")
                    )
            ) }

        private val permanentHistory =
            RoutingHistory.from(routingElements)

        override fun remove(identifier: Identifier) {
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
        override fun remove(identifier: Identifier) {
            // no-op -- it's empty
        }

        override fun subscribe(observer: Observer<in RoutingHistory<C>>) =
            Observable.empty<RoutingHistory<C>>().subscribe(observer)
    }

    // TODO extract
    class Pool<C : Parcelable>(
        private val allowRepeatingConfigurations: Boolean
    ) : RoutingSource<C> {
        private var elements: Map<Identifier, RoutingHistoryElement<C>> = emptyMap()
        private val current: RoutingHistory<C>
            get() = RoutingHistory.from(elements.values)

        private val states: Relay<RoutingHistory<C>> = BehaviorRelay.createDefault(
            RoutingHistory.from(current)
        )

        fun add(
            configuration: C,
            identifier: Identifier = Identifier(
                "Set ${System.identityHashCode(this)} #$configuration"
            )
        ): Identifier {
            if (!allowRepeatingConfigurations) {
                elements
                    .filter { it.value.routing.configuration == configuration }
                    .map { it.key }
                    .firstOrNull()?.let {
                        return it
                    }
            }

            elements = elements.plus(
                identifier to RoutingHistoryElement(
                    activation = INACTIVE,
                    routing = Routing(
                        configuration = configuration,
                        identifier = identifier
                    ),
                    // TODO consider overlay support -- not needed now, can be added later
                    overlays = emptyList()
                )
            )

            updateState()

            return identifier
        }

        fun activate(identifier: Identifier) {
            updateActivation(identifier, ACTIVE)
        }

        fun deactivate(identifier: Identifier) {
            updateActivation(identifier, INACTIVE)
        }

        private fun updateActivation(identifier: Identifier, activation: Activation) {
            elements[identifier]?.let {
                elements = elements
                    .minus(it.routing.identifier)
                    .plus(it.routing.identifier to it.copy(
                        activation = activation
                    )
                )
                updateState()
            }
        }

        override fun remove(identifier: Identifier) {
            elements[identifier]?.let {
                elements = elements
                    .minus(it.routing.identifier)

                updateState()
            }
        }

        private fun updateState() {
            states.accept(current)
        }

        override fun subscribe(observer: Observer<in RoutingHistory<C>>) =
            states.subscribe(observer)
    }
}

