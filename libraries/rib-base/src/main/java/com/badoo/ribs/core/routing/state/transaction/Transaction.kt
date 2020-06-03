package com.badoo.ribs.core.routing.state.transaction

import android.os.Parcelable
import com.badoo.ribs.core.routing.state.transaction.RoutingCommand.Add
import com.badoo.ribs.core.routing.state.transaction.RoutingCommand.Remove
import com.badoo.ribs.core.routing.state.action.multi.PoolAction
import com.badoo.ribs.core.routing.state.action.multi.SaveInstanceStateAction
import com.badoo.ribs.core.routing.state.action.multi.SleepAction
import com.badoo.ribs.core.routing.state.action.multi.WakeUpAction
import com.badoo.ribs.core.routing.state.feature.TransitionDescriptor
import com.badoo.ribs.core.routing.history.Routing

internal sealed class Transaction<C : Parcelable> {

    sealed class PoolCommand<C : Parcelable>(
        val action: PoolAction<C>
    ) : Transaction<C>() {
        class Sleep<C : Parcelable> : PoolCommand<C>(SleepAction())
        class WakeUp<C : Parcelable> : PoolCommand<C>(WakeUpAction())
        class SaveInstanceState<C : Parcelable> : PoolCommand<C>(SaveInstanceStateAction())
    }

    data class RoutingChangeset<C : Parcelable>(
        val descriptor: TransitionDescriptor,
        val commands: List<RoutingCommand<C>>
    ) : Transaction<C>()

    companion object {
        fun <C : Parcelable> from(vararg commands: RoutingCommand<out C>): Transaction<C> =
            RoutingChangeset(
                descriptor = TransitionDescriptor.None,
                commands = commands.toList() as List<RoutingCommand<C>>
            )
    }
}

internal fun <C : Parcelable> List<RoutingCommand<C>>.addedOrRemoved(routing: Routing<C>): Boolean =
    any { it.routing == routing && (it is Add || it is Remove) }
