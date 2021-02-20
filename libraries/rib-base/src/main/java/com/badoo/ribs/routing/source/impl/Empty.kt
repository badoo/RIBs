package com.badoo.ribs.routing.source.impl

import android.os.Parcelable
import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistory
import com.badoo.ribs.routing.source.RoutingSource

class Empty<C : Parcelable> : RoutingSource<C> {

    override fun baseLineState(fromRestored: Boolean): RoutingHistory<C> =
        RoutingHistory.from(
            emptySet()
        )

    override fun remove(identifier: Routing.Identifier) {
        // no-op -- it's empty
    }

    override fun observe(callback: (RoutingHistory<C>) -> Unit): Cancellable =
        Cancellable.Empty
}
