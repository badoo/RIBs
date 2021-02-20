package com.badoo.ribs.routing.source

import android.os.Parcelable
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.plugin.UpNavigationHandler
import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistory
import com.badoo.ribs.routing.source.impl.Combined
import com.badoo.ribs.routing.source.impl.Empty
import com.badoo.ribs.routing.source.impl.Permanent

/**
 * Represents a source that emits [RoutingHistory].
 *
 * Implementations will be notified to handle back press, so that they can change their
 *  internal [RoutingHistory]] as a reaction.
 *
 * Implementations will be notified to save their state.
 */
interface RoutingSource<C : Parcelable> :
    Source<RoutingHistory<C>>,
    SubtreeBackPressHandler,
    UpNavigationHandler,
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

    /**
     * Should remove any specific [Routing] from the current [RoutingHistory] by its id.
     */
    fun remove(identifier: Routing.Identifier)

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

