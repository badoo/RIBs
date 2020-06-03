package com.badoo.ribs.core.routing.source

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.routing.ConcatIterator
import com.badoo.ribs.core.routing.history.Routing.Identifier
import com.badoo.ribs.core.routing.history.RoutingHistory
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.BiFunction

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

}

