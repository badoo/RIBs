package com.badoo.ribs.core.routing.history

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Activate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature


internal object RoutingHistoryDiffer {

    /**
     * Calculates diff between two states of [BackStackFeature] (where each state contains the current list
     * of [C] configurations representing the back stack), and translates the diff to
     * a list of [ConfigurationCommand]s that represent getting from the old state to the new one.
     */
    @SuppressWarnings("NestedBlockDepth", "LongMethod") // TODO will be reworked
    fun <C : Parcelable> diff(
        previous: RoutingHistory<C>,
        current: RoutingHistory<C>
    ): Set<ConfigurationCommand<C>> =
        when (current) {
            previous -> emptySet()
            else -> {
                val commands = mutableSetOf<ConfigurationCommand<C>>()
                val previousIds = previous.map { it.routing.identifier }
                val currentIds = current.map { it.routing.identifier }

                // TODO extract remove
                previousIds.minus(currentIds).reversed().forEach { identifier ->
                    val toRemove = previous.find { it.routing.identifier == identifier }!!
                    if (toRemove.activation == RoutingHistoryElement.Activation.ACTIVE) {
                        commands += Deactivate(toRemove.routing)
                    }
                    commands += Remove(toRemove.routing)
                    toRemove.overlays.reversed().forEach { overlay ->
                        commands += Deactivate(overlay)
                        commands += Remove(overlay)
                    }
                }

                // TODO extract add
                currentIds.minus(previousIds).forEach { identifier ->
                    val toAdd = current.find { it.routing.identifier == identifier }!!
                    commands += Add(toAdd.routing)

                    // FIXME better abstraction for transitions between various Activations
                    if (toAdd.activation == RoutingHistoryElement.Activation.ACTIVE) {
                        commands += Activate(toAdd.routing)

                    }

                    toAdd.overlays.forEach { overlay ->
                        commands += Add(overlay)

                        if (toAdd.activation == RoutingHistoryElement.Activation.ACTIVE) {
                            commands += Activate(overlay)
                        }
                    }
                }

                // TODO extract modify
                currentIds.intersect(previousIds).forEach { identifier ->
                    val t0 = previous.find { it.routing.identifier == identifier }!!
                    val t1 = current.find { it.routing.identifier == identifier }!!

                    if (t1.activation != t0.activation) {
                        when (t1.activation) {
                            RoutingHistoryElement.Activation.ACTIVE -> {
                                commands += Activate(t1.routing)
                                t1.overlays.forEach { overlay ->
                                    commands += Activate(overlay)
                                }
                            }
                            RoutingHistoryElement.Activation.INACTIVE -> {
                                commands += Deactivate(t1.routing)
                                t1.overlays.forEach { overlay ->
                                    commands += Deactivate(overlay)
                                }
                            }
                            RoutingHistoryElement.Activation.SHRUNK -> Unit // FIXME implement
                        }
                    }

                    if (t1.routing.meta != t0.routing.meta) {
                        // FIXME implement
                    }

                    if (t1.overlays != t0.overlays) {
                        // TODO make note in public doc that overlay indices are meant to be stable
                        t0.overlays.minus(t1.overlays).forEach { overlay ->
                            commands += Deactivate(overlay)
                            commands += Remove(overlay)
                        }

                        t1.overlays.minus(t0.overlays).forEach { overlay ->
                            commands += Add(overlay)
                            commands += Activate(overlay)
                        }
                    }
                }

                commands
            }
        }
}

