package com.badoo.ribs.core.routing.source

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.routing.ConcatIterator
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
import java.util.UUID

interface RoutingSource<C : Parcelable> :
    ObservableSource<RoutingHistory<C>>,
    SubtreeBackPressHandler,
    SavesInstanceState {

    /**
     * Baseline will be used for diffing all further emissions against.
     *
     * It is intended to report an empty state on new instance creation.
     *
     * If the [RoutingSource] implements its own persistence however, then it should report the restored
     * state as a baseline, otherwise all of its restored elements will be considered "new" when
     * diffing against empty state, and as a result, added again.
     *
     * @param fromRestored permanent sources can use this information to set correct baseline
     */
    fun baseLineState(fromRestored: Boolean): RoutingHistory<C>

    fun remove(identifier: Identifier)

    operator fun plus(other: RoutingSource<C>): RoutingSource<C> =
        Combined(this, other)

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
                CombinedHistory(
                    source1,
                    source2
                )
            }
        )

        override fun baseLineState(fromRestored: Boolean): RoutingHistory<C> =
            CombinedHistory(
                first.baseLineState(fromRestored),
                second.baseLineState(fromRestored)
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

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            first.onSaveInstanceState(outState)
            second.onSaveInstanceState(outState)
        }
    }

    // TODO extract
    class Permanent<C : Parcelable>(
        permanents: Iterable<C>
    ) : RoutingSource<C> {

        companion object {
            fun <C : Parcelable> permanent(permanents: Iterable<C>) =
                Permanent(
                    permanents
                )

            fun <C : Parcelable> permanent(vararg permanents: C) =
                Permanent(
                    permanents.toSet()
                )
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
            Observable.just(RoutingHistory.from(routingElements))

        override fun baseLineState(fromRestored: Boolean): RoutingHistory<C> =
            if (fromRestored) {
                RoutingHistory.from(routingElements)
            } else {
                RoutingHistory.from(emptySet())
            }

        override fun remove(identifier: Identifier) {
            // no-op -- it's permanent!
        }

        override fun subscribe(observer: Observer<in RoutingHistory<C>>) {
            permanentHistory
                .subscribe(observer)
        }
    }

    // TODO extract
    class Empty<C : Parcelable> :
        RoutingSource<C> {

        override fun baseLineState(fromRestored: Boolean): RoutingHistory<C>  =
            RoutingHistory.from(emptySet())

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

        // FIXME save/restore to bundle to support correct baseLineState
        override fun baseLineState(fromRestored: Boolean): RoutingHistory<C> =
            RoutingHistory.from(emptySet())

        fun add(
            configuration: C,
            identifier: Identifier = Identifier(
                "Set ${System.identityHashCode(this)} #$configuration.${if (allowRepeatingConfigurations) UUID.randomUUID() else "#"}"
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

