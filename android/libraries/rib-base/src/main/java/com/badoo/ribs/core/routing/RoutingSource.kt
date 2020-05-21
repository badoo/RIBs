package com.badoo.ribs.core.routing

import android.os.Parcelable
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistory
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer

interface RoutingSource<C : Parcelable> :
    ObservableSource<RoutingHistory<C>>,
    SubtreeBackPressHandler {

    fun remove(identifier: Routing.Identifier)

    class Permanent<C : Parcelable>(permanents: Set<C>) : RoutingSource<C> {

        private val routingElements = permanents.map {
            RoutingHistoryElement(Routing(it))
        }

        private val permanentHistory = RoutingHistory.from(routingElements)

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
