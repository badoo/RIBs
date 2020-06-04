package com.badoo.ribs.routing.history

import android.os.Parcelable

interface RoutingHistory<C : Parcelable> : Iterable<RoutingHistoryElement<C>> {

    companion object {
        fun <C : Parcelable> from(iterable: Iterable<RoutingHistoryElement<C>>): RoutingHistory<C> =
            IterableHistory(iterable)
    }

    data class IterableHistory<C : Parcelable>(
        val iterable: Iterable<RoutingHistoryElement<C>>
    ) : RoutingHistory<C>, Iterable<RoutingHistoryElement<C>> by iterable
}
