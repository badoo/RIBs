package com.badoo.ribs.core.routing.source

import android.os.Parcelable
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.routing.Routing.Identifier
import com.badoo.ribs.core.routing.history.RoutingHistory
import com.badoo.ribs.core.routing.source.impl.Combined
import com.badoo.ribs.core.routing.source.impl.Empty
import com.badoo.ribs.core.routing.source.impl.Permanent
import io.reactivex.ObservableSource

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

    companion object {
        fun <C : Parcelable> empty() =
            Empty<C>()

        fun <C : Parcelable> permanent(permanents: Iterable<C>) =
            Permanent(permanents)

        fun <C : Parcelable> permanent(vararg permanents: C) =
            Permanent(permanents.toSet())
    }
}

