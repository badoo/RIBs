package com.badoo.ribs.core.routing.history

import android.os.Parcelable

interface RoutingHistory<C : Parcelable> : Iterable<RoutingHistoryElement<C>> {

    companion object {
        fun <C : Parcelable> from(iterable: Iterable<RoutingHistoryElement<C>>): RoutingHistory<C> =
            object : RoutingHistory<C>, Iterable<RoutingHistoryElement<C>> by iterable {}
    }
}
