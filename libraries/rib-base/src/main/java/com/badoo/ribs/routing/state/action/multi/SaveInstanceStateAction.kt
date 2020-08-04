package com.badoo.ribs.routing.state.action.multi

import android.os.Parcelable
import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.state.RoutingContext.Resolved
import com.badoo.ribs.routing.state.action.TransactionExecutionParams
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect
import com.badoo.ribs.routing.state.feature.state.WorkingState

/**
 * Calls saveInstanceState() on all Nodes associated with Resolved configurations in the pool
 */
internal class SaveInstanceStateAction<C : Parcelable> : PoolAction<C> {

    /**
     * Finds [Resolved] elements in the pool and maps them to the value returned by calling
     * [Resolved.saveInstanceState] on them.
     *
     * It's not necessary to handle [RoutingContext.Unresolved] elements,
     * as they do not contain any Nodes that could have had a chance to change their states.
     *
     * @return the map of found elements with updated bundles
     */
    override fun execute(
        state: WorkingState<C>,
        params: TransactionExecutionParams<C>
    ) {
        val updatedElements = state.pool
            .filterValues { it is Resolved<C> }
            .mapValues { (_, value) ->
                (value as Resolved<C>).saveInstanceState()
            }

        params.emitter.invoke(Effect.Global.SaveInstanceState(updatedElements = updatedElements))
    }
}
