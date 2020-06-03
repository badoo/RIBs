package com.badoo.ribs.core.routing.history

import android.os.Parcelable
import com.badoo.ribs.core.routing.Routing
import com.badoo.ribs.core.routing.state.changeset.RoutingCommand
import com.badoo.ribs.core.routing.state.changeset.RoutingCommand.Activate
import com.badoo.ribs.core.routing.state.changeset.RoutingCommand.Add
import com.badoo.ribs.core.routing.state.changeset.RoutingCommand.Deactivate
import com.badoo.ribs.core.routing.state.changeset.RoutingCommand.Remove
import com.badoo.ribs.core.routing.source.backstack.BackStackFeature
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.ACTIVE
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.INACTIVE
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.SHRUNK


internal object RoutingHistoryDiffer {

    /**
     * Calculates diff between two states of [BackStackFeature] (where each state contains the current list
     * of [C] configurations representing the back stack), and translates the diff to
     * a list of [RoutingCommand]s that represent getting from the old state to the new one.
     */
    @SuppressWarnings("NestedBlockDepth", "LongMethod") // TODO will be reworked
    fun <C : Parcelable> diff(
        previous: RoutingHistory<C>,
        current: RoutingHistory<C>
    ): Set<RoutingCommand<C>> =
        when (current) {
            previous -> emptySet()
            else -> {
                val commands = mutableSetOf<RoutingCommand<C>>()
                val previousIds = previous.map { it.routing.identifier }
                val currentIds = current.map { it.routing.identifier }

                previousIds.minus(currentIds).reversed().forEach { identifier ->
                    val toRemove = previous.find(identifier)!!
                    commands += if (toRemove.activation == ACTIVE) toRemove.deactivate() else emptySet()
                    commands += toRemove.remove()
                }

                currentIds.minus(previousIds).forEach { identifier ->
                    val toAdd = current.find(identifier)!!
                    commands += toAdd.add()
                    commands += if (toAdd.activation == ACTIVE) toAdd.activate() else emptySet()
                }

                // TODO extract modify
                currentIds.intersect(previousIds).forEach { identifier ->
                    val t0 = previous.find { it.routing.identifier == identifier }!!
                    val t1 = current.find { it.routing.identifier == identifier }!!

                    commands += diff(t0, t1)
                }

                commands
            }
        }

    private fun <C : Parcelable> RoutingHistory<C>.find(identifier: Routing.Identifier): RoutingHistoryElement<C>? =
        find { it.routing.identifier == identifier }

    private fun <C : Parcelable> RoutingHistoryElement<C>.remove(): Set<RoutingCommand<C>> {
        val commands = mutableSetOf<RoutingCommand<C>>()
        commands += Remove(routing)
        commands += overlays.map { Remove(it) }
        return commands
    }

    private fun <C : Parcelable> RoutingHistoryElement<C>.deactivate(): Set<RoutingCommand<C>> {
        val commands = mutableSetOf<RoutingCommand<C>>()
        commands += Deactivate(routing)
        commands += overlays.map { Deactivate(it) }
        return commands
    }

    private fun <C : Parcelable> RoutingHistoryElement<C>.add(): Set<RoutingCommand<C>> {
        val commands = mutableSetOf<RoutingCommand<C>>()
        commands += Add(routing)
        commands += overlays.map { Add(it) }
        return commands
    }

    private fun <C : Parcelable> RoutingHistoryElement<C>.activate(): Set<RoutingCommand<C>> {
        val commands = mutableSetOf<RoutingCommand<C>>()
        commands += Activate(routing)
        commands += overlays.map { Activate(it) }
        return commands
    }

    private fun <C : Parcelable> diff(t0: RoutingHistoryElement<C>, t1: RoutingHistoryElement<C>): Set<RoutingCommand<C>> {
        val commands = mutableSetOf<RoutingCommand<C>>()

        commands += diffActivationChange(t0, t1)
        commands += diffMetaChange(t0, t1)
        commands += diffOverlayChange(t0, t1)

        return commands
    }

    private fun <C : Parcelable> diffActivationChange(t0: RoutingHistoryElement<C>, t1: RoutingHistoryElement<C>): Set<RoutingCommand<C>> {
        val commands = mutableSetOf<RoutingCommand<C>>()

        if (t1.activation != t0.activation) {
            commands += when (t1.activation) {
                ACTIVE -> t1.activate()
                INACTIVE -> t1.deactivate()
                SHRUNK -> emptySet()// FIXME implement
            }
        }

        return commands
    }

    private fun <C : Parcelable> diffMetaChange(t0: RoutingHistoryElement<C>, t1: RoutingHistoryElement<C>): Set<RoutingCommand<C>> {
        val commands = mutableSetOf<RoutingCommand<C>>()

        if (t1.routing.meta != t0.routing.meta) {
            // FIXME implement
        }

        return commands
    }

    private fun <C : Parcelable> diffOverlayChange(t0: RoutingHistoryElement<C>, t1: RoutingHistoryElement<C>): Set<RoutingCommand<C>> {
        val commands = mutableSetOf<RoutingCommand<C>>()

        if (t1.overlays != t0.overlays) {
            // TODO make note in public doc that overlay indices are meant to be stable
            t0.overlays.minus(t1.overlays).forEach { overlay ->
                if (t0.activation == ACTIVE) commands += Deactivate(overlay)
                commands += Remove(overlay)
            }

            t1.overlays.minus(t0.overlays).forEach { overlay ->
                commands += Add(overlay)
                if (t0.activation == ACTIVE) commands += Activate(overlay)
            }
        }

        return commands
    }
}

