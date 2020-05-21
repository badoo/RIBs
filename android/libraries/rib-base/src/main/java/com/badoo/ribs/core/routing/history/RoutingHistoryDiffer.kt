package com.badoo.ribs.core.routing.history

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Activate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Content
import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Overlay
import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Overlay.Key
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature


internal object RoutingHistoryDiffer {

    /**
     * Calculates diff between two states of [BackStackFeature] (where each state contains the current list
     * of [C] configurations representing the back stack), and translates the diff to
     * a list of [ConfigurationCommand]s that represent getting from the old state to the new one.
     */
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
                    val key = Content(identifier.id, toRemove.routing) // FIXME keys
                    if (toRemove.activation == RoutingHistoryElement.Activation.ACTIVE) {
                        commands += Deactivate(key)
                    }
                    commands += Remove(key)
                    toRemove.overlays.reversed().forEachIndexed { overlayIndex, overlayConfiguration ->
                        val overlayKey = Overlay(Key(key, overlayIndex, overlayConfiguration))
                        commands += Deactivate(overlayKey)
                        commands += Remove(overlayKey)
                    }
                }

                // TODO extract add
                currentIds.minus(previousIds).forEach { identifier ->
                    val toAdd = current.find { it.routing.identifier == identifier }!!
                    val key = Content(identifier.id, toAdd.routing) // FIXME keys
                    commands += Add(key)

                    // FIXME better abstraction for transitions between various Activations
                    if (toAdd.activation == RoutingHistoryElement.Activation.ACTIVE) {
                        commands += Activate(key)

                    }

                    toAdd.overlays.forEachIndexed { overlayIndex, overlayConfiguration ->
                        val overlayKey = Overlay(Key(key, overlayIndex, overlayConfiguration))
                        commands += Add(overlayKey)

                        // FIXME better abstraction for transitions between various Activations
                        if (toAdd.activation == RoutingHistoryElement.Activation.ACTIVE) {
                            commands += Activate(overlayKey)
                        }
                    }
                }

                // TODO extract modify
                currentIds.intersect(previousIds).forEach { identifier ->
                    val t0 = previous.find { it.routing.identifier == identifier }!!
                    val t1 = current.find { it.routing.identifier == identifier }!!
                    val key = Content(identifier.id, t1.routing) // FIXME keys

                    if (t1.activation != t0.activation) {
                        when (t1.activation) {
                            RoutingHistoryElement.Activation.ACTIVE -> {
                                commands += Activate(key)
                                t1.overlays.forEachIndexed { overlayIndex, overlayConfiguration ->
                                    val overlayKey = Overlay(Key(key, overlayIndex, overlayConfiguration))
                                    commands += Activate(overlayKey)
                                }
                            }
                            RoutingHistoryElement.Activation.INACTIVE -> {
                                commands += Deactivate(key)
                                t1.overlays.forEachIndexed { overlayIndex, overlayConfiguration ->
                                    val overlayKey = Overlay(Key(key, overlayIndex, overlayConfiguration))
                                    commands += Deactivate(overlayKey)
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
                        t0.overlays.minus(t1.overlays).forEachIndexed { overlayIndex, overlayConfiguration ->
                            val overlayKey = Overlay(Key(key, overlayIndex, overlayConfiguration))
                            commands += Deactivate(overlayKey)
                            commands += Remove(overlayKey)
                        }

                        t1.overlays.minus(t0.overlays).forEachIndexed { overlayIndex, overlayConfiguration ->
                            val overlayKey = Overlay(Key(key, overlayIndex, overlayConfiguration))
                            commands += Add(overlayKey)
                            commands += Activate(overlayKey)
                        }
                    }
                }

                // TODO remove internal fields OR decompose RoutingElement field by field into ConfigurationKey

//                previous.forEach { old ->
//                    val new = current.find { it.routing.identifier == old.identifier })
//                    if (new == null) {
//
//                    } else {
//                        if (new.activation != old.activation) {
//                            // TODO create command
//                        }
//                        if (new.meta != old.meta) {
//
//                        }
//                    }
//                }

                commands
            }
        }
//
//    private fun findIndexOfLastCommonConfiguration(
//        old: List<RoutingElement<*>>,
//        new: List<RoutingElement<*>>
//    ): Int {
//        var idx = -1
//        val lastCommonIndex = min(old.lastIndex, new.lastIndex)
//
//        for (i in 0..lastCommonIndex) {
//            // Compare only configurations, as if it's only the overlays that are different, they will be handled later
//            if (new.elementAt(i).configuration != old.elementAt(i).configuration) {
//                return idx
//            }
//
//            idx = i
//        }
//
//        return idx
//    }
//
//    private fun <C : Parcelable> BackStack<C>.deactivateIfNeeded(other: BackStack<C>): List<ConfigurationCommand<C>> =
//        when {
//            contentListHasChanged(other) && isNotEmpty() -> {
//                val contentKey = Content(lastIndex, last())
//                val commands = mutableListOf<ConfigurationCommand<C>>()
//                commands += last().deactivateAllOverlays(contentKey)
//                commands += Deactivate(contentKey)
//                commands
//            }
//            else -> emptyList()
//        }
//
//    /**
//     * A comparison between two lists that doesn't care about overlay differences, only the direct elements
//     */
//    private fun <C : Parcelable> BackStack<C>.contentListHasChanged(other: BackStack<C>) =
//        map { it.configuration } != other.map { it.configuration }
//
//    private fun <C : Parcelable> BackStack<C>.removeUntil(targetIdxExclusive: Int): List<ConfigurationCommand<C>> {
//        val offset = targetIdxExclusive + 1
//        return subList(offset, size)
//            .mapIndexed { contentIndex, backStackElement ->
//                val commands = mutableListOf<ConfigurationCommand<C>>()
//                val realIndex = offset + contentIndex
//                val contentKey = Content(realIndex, backStackElement)
//                commands += Remove(contentKey)
//                commands += backStackElement.removeAllOverlays(contentKey).reversed()
//                if (realIndex != lastIndex) {
//                    commands += backStackElement.deactivateAllOverlays(contentKey).reversed()
//                }
//
//                commands
//            }
//            .flatten()
//            .reversed()
//    }
//
//    private fun <C : Parcelable> BackStack<C>.addFrom(targetIdxExclusive: Int): List<ConfigurationCommand<C>> {
//        val offset = targetIdxExclusive + 1
//        return subList(offset, size)
//            .mapIndexed { contentIndex, backStackElement ->
//                val commands = mutableListOf<ConfigurationCommand<C>>()
//                val contentKey = Content(offset + contentIndex, backStackElement)
//                commands += Add(contentKey)
//                commands += backStackElement.addAllOverlays(contentKey)
//                commands
//            }
//            .flatten()
//    }
//
//    private fun <C : Parcelable> BackStack<C>.activateIfNeeded(other: BackStack<C>): List<ConfigurationCommand<C>> =
//        when {
//            isNotEmpty() && contentListHasChanged(other) -> {
//                val commands = mutableListOf<ConfigurationCommand<C>>()
//                val contentKey = Content(lastIndex, last())
//                val activate = Activate<C>(contentKey)
//                commands += activate
//                commands += last().activateAllOverlays(contentKey)
//                commands
//            }
//            else -> emptyList()
//        }
//
//    private fun <C : Parcelable> RoutingElement<C>.addAllOverlays(content: Content<C>): List<ConfigurationCommand<C>> =
//        overlays.mapIndexed { overlayIndex, overlayConfiguration ->
//            Add(Overlay(Key(content, overlayIndex, overlayConfiguration)))
//        }
//
//    private fun <C : Parcelable> RoutingElement<C>.activateAllOverlays(contentKey: Content<C>): List<ConfigurationCommand<C>> =
//        overlays.mapIndexed { overlayIndex, overlayConfiguration ->
//            Activate<C>(Overlay(Key(contentKey, overlayIndex, overlayConfiguration)))
//        }
//
//    private fun <C : Parcelable> RoutingElement<C>.deactivateAllOverlays(contentKey: Content<C>): List<ConfigurationCommand<C>> =
//        overlays
//            .mapIndexed { overlayIndex, overlayConfiguration ->
//                Deactivate<C>(Overlay(Key(contentKey, overlayIndex, overlayConfiguration)))
//            }
//            .reversed()
//
//    private fun <C : Parcelable> RoutingElement<C>.removeAllOverlays(contentKey: Content<C>): List<ConfigurationCommand<C>> =
//        overlays
//            .mapIndexed { overlayIndex, overlayConfiguration ->
//                Remove<C>(Overlay(Key(contentKey, overlayIndex, overlayConfiguration)))
//            }
//            .reversed()
//
//    private fun <C : Parcelable> BackStack<C>.overlayDiff(old: BackStack<C>): List<ConfigurationCommand<C>> {
//        val commands = mutableListOf<ConfigurationCommand<C>>()
//
//        forEachIndexed { index, newElement ->
//            val oldElement = old.getOrNull(index)
//            val contentKey = Content<C>(index, newElement)
//            commands += oldElement?.removeNecessaryOverlays(newElement, contentKey) ?: emptyList()
//            commands += newElement.addNecessaryOverlays(oldElement, contentKey)
//        }
//
//        return commands
//    }
//
//    /**
//     * Old -> New
//     *
//     * Compares an old back stack element to a new back stack element and reflects changes.
//     *
//     * Difference will be removed from Old.
//     */
//    private fun <C : Parcelable> RoutingElement<C>.removeNecessaryOverlays(
//        newElement: RoutingElement<C>,
//        contentKey: Content<C>
//    ): List<ConfigurationCommand<C>> =
//        overlays
//            .mapIndexed { index, oldElement ->
//                if (oldElement == newElement.overlayAt(index)) emptyList()
//                else listOf(
//                    Deactivate<C>(Overlay(Key(contentKey, index, oldElement))),
//                    Remove<C>(Overlay(Key(contentKey, index, oldElement)))
//                )
//            }
//            .reversed()
//            .flatten()
//
//    /**
//     * New -> Old
//     *
//     * Compares a new back stack element to an old back stack element and reflects changes.
//     *
//     * Difference will be added to New.
//     */
//    private fun <C : Parcelable> RoutingElement<C>.addNecessaryOverlays(
//        oldElement: RoutingElement<C>?,
//        contentKey: Content<C>
//    ): List<ConfigurationCommand<C>> =
//        overlays
//            .mapIndexed { index, newElement ->
//                if (newElement == oldElement?.overlayAt(index)) emptyList()
//                else listOf(
//                    Add(Overlay(Key(contentKey, index, newElement))),
//                    Activate<C>(Overlay(Key(contentKey, index, newElement)))
//                )
//            }
//            .flatten()
//
//    private fun <C : Parcelable> RoutingElement<C>.overlayAt(index: Int): RoutingElement<C>? =
//        overlays.getOrNull(index)
}

