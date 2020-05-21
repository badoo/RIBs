package com.badoo.ribs.core.routing.configuration.feature

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.feature.operation.BackStack
import com.badoo.ribs.core.routing.history.RoutingHistory
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import kotlinx.android.parcel.Parcelize


@Parcelize
data class BackStackFeatureState<C : Parcelable>(
    val backStack: BackStack<C> = emptyList()
) : Parcelable, RoutingHistory<C> {

    val current: RoutingHistoryElement<C>?
        get() = backStack.lastOrNull()

    override fun iterator(): Iterator<RoutingHistoryElement<C>> =
        backStack.iterator()
}


