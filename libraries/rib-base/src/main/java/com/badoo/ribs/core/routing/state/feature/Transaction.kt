package com.badoo.ribs.core.routing.state.feature

import android.os.Parcelable
import com.badoo.ribs.core.routing.state.action.multi.PoolAction
import com.badoo.ribs.core.routing.state.action.multi.SaveInstanceStateAction
import com.badoo.ribs.core.routing.state.action.multi.SleepAction
import com.badoo.ribs.core.routing.state.action.multi.WakeUpAction
import com.badoo.ribs.core.routing.state.transaction.RoutingChangeset
import com.badoo.ribs.core.routing.state.transaction.RoutingCommand
import com.badoo.ribs.core.routing.state.transaction.TransitionDescriptor

internal sealed class Transaction<C : Parcelable> {

    sealed class PoolCommand<C : Parcelable>(
        val action: PoolAction<C>
    ) : Transaction<C>() {
        class Sleep<C : Parcelable> : PoolCommand<C>(SleepAction())
        class WakeUp<C : Parcelable> : PoolCommand<C>(WakeUpAction())
        class SaveInstanceState<C : Parcelable> : PoolCommand<C>(SaveInstanceStateAction())
    }

    data class RoutingChange<C : Parcelable>(
        val descriptor: TransitionDescriptor,
        val changeset: RoutingChangeset<C>
    ) : Transaction<C>()

    companion object {
        fun <C : Parcelable> from(vararg commands: RoutingCommand<out C>): Transaction<C> =
            RoutingChange(
                descriptor = TransitionDescriptor.None,
                changeset = commands.toList() as RoutingChangeset<C>
            )
    }
}
