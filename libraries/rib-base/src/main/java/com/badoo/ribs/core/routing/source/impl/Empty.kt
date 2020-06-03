package com.badoo.ribs.core.routing.source.impl

import android.os.Parcelable
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistory
import com.badoo.ribs.core.routing.source.RoutingSource
import io.reactivex.Observable
import io.reactivex.Observer

class Empty<C : Parcelable> : RoutingSource<C> {

    override fun baseLineState(fromRestored: Boolean): RoutingHistory<C> =
        RoutingHistory.from(
            emptySet()
        )

    override fun remove(identifier: Routing.Identifier) {
        // no-op -- it's empty
    }

    override fun subscribe(observer: Observer<in RoutingHistory<C>>) =
        Observable.empty<RoutingHistory<C>>()
            .subscribe(observer)
}
