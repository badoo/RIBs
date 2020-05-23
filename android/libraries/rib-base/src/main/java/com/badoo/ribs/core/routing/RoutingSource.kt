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
import java.util.ArrayDeque

interface RoutingSource<C : Parcelable> :
    ObservableSource<RoutingHistory<C>>,
    SubtreeBackPressHandler {

    operator fun plus(second: RoutingSource<C>): RoutingSource<C> {
        val first = this

        val combined = Observable.combineLatest(
            this,
            second,
            BiFunction<RoutingHistory<C>, RoutingHistory<C>, RoutingHistory<C>> { source1, source2 ->
                object : RoutingHistory<C> {
                    override fun iterator(): Iterator<RoutingHistoryElement<C>> =
                        ConcatIterator(source1.iterator()) + source2.iterator()
                }
            }
        )

        return object : RoutingSource<C> {
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

// TODO extract
// https://dev.to/alediaferia/a-concatenated-iterator-example-in-kotlin-1l23
class ConcatIterator<T>(iterator: Iterator<T>) : Iterator<T> {
    private val store = ArrayDeque<Iterator<T>>()

    init {
        if (iterator.hasNext())
            store.add(iterator)
    }

    override fun hasNext(): Boolean = when {
        store.isEmpty() -> false
        else -> store.first.hasNext()
    }

    override fun next(): T {
        val t = store.first.next()

        if (!store.first.hasNext())
            store.removeFirst()

        return t
    }

    operator fun plus(iterator: Iterator<T>): ConcatIterator<T> {
        if (iterator.hasNext())
            store.add(iterator)
        return this
    }
}

operator fun <T> Iterator<T>.plus(iterator: Iterator<T>): ConcatIterator<T> =
    when {
        this is ConcatIterator<T> -> this.plus(iterator)
        iterator is ConcatIterator<T> -> iterator.plus(this)
        else -> ConcatIterator(this).plus(iterator)
    }
