package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Activate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.configuration.feature.operation.BackStack
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import java.lang.Math.min


@Deprecated("Left here for comparison only. Use RoutingHistoryDiffer instead")
internal object ConfigurationCommandCreator {

    /**
     * Calculates diff between two states of [BackStackFeature] (where each state contains the current list
     * of [C] configurations representing the back stack), and translates the diff to
     * a list of [ConfigurationCommand]s that represent getting from the old state to the new one.
     */
    @Deprecated("Left here for comparison only. Use RoutingHistoryDiffer.diff instead")
    fun <C : Parcelable> diff(
        oldStack: BackStack<C>,
        newStack: BackStack<C>
    ): List<ConfigurationCommand<C>> =
        when (newStack) {
            oldStack -> emptyList()
            else -> {
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val indexOfLastCommonConfiguration =
                    findIndexOfLastCommonConfiguration(oldStack, newStack)
                commands += oldStack.deactivateIfNeeded(newStack)
                commands += oldStack.removeUntil(indexOfLastCommonConfiguration)
                commands += newStack.addFrom(indexOfLastCommonConfiguration)
                commands += newStack.activateIfNeeded(oldStack)
                commands += newStack.overlayDiff(oldStack)
                commands
            }
        }

    private fun findIndexOfLastCommonConfiguration(
        oldStack: List<RoutingHistoryElement<*>>,
        newStack: List<RoutingHistoryElement<*>>
    ): Int {
        var idx = -1
        val lastCommonIndex = min(oldStack.lastIndex, newStack.lastIndex)

        for (i in 0..lastCommonIndex) {
            // Compare only configurations, as if it's only the overlays that are different, they will be handled later
            if (newStack.elementAt(i).routing.configuration != oldStack.elementAt(i).routing.configuration) {
                return idx
            }

            idx = i
        }

        return idx
    }

    private fun <C : Parcelable> BackStack<C>.deactivateIfNeeded(other: BackStack<C>): List<ConfigurationCommand<C>> =
        when {
            contentListHasChanged(other) && isNotEmpty() -> {
                val contentKey = last().routing
                val commands = mutableListOf<ConfigurationCommand<C>>()
                commands += last().overlays.map { Deactivate(it) }
                commands += Deactivate(contentKey)
                commands
            }
            else -> emptyList()
        }

    /**
     * A comparison between two lists that doesn't care about overlay differences, only the direct elements
     */
    private fun <C : Parcelable> BackStack<C>.contentListHasChanged(other: BackStack<C>) =
        map { it.routing.configuration } != other.map { it.routing.configuration }

    private fun <C : Parcelable> BackStack<C>.removeUntil(targetIdxExclusive: Int): List<ConfigurationCommand<C>> {
        val offset = targetIdxExclusive + 1
        return subList(offset, size)
            .mapIndexed { contentIndex, backStackElement ->
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val realIndex = offset + contentIndex
                val contentKey = backStackElement.routing
                commands += Remove(contentKey)
                commands += backStackElement.overlays.map { Remove(it) }.reversed()
                if (realIndex != lastIndex) {
                    commands += backStackElement.overlays.map { Deactivate(it) }.reversed()
                }

                commands
            }
            .flatten()
            .reversed()
    }

    private fun <C : Parcelable> BackStack<C>.addFrom(targetIdxExclusive: Int): List<ConfigurationCommand<C>> {
        val offset = targetIdxExclusive + 1
        return subList(offset, size)
            .mapIndexed { contentIndex, backStackElement ->
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val contentKey = backStackElement.routing
                commands += Add(contentKey)
                commands += backStackElement.overlays.map { Add(it) }
                commands
            }
            .flatten()
    }

    private fun <C : Parcelable> BackStack<C>.activateIfNeeded(other: BackStack<C>): List<ConfigurationCommand<C>> =
        when {
            isNotEmpty() && contentListHasChanged(other) -> {
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val contentKey = last().routing
                val activate = Activate(contentKey)
                commands += activate
                commands += last().overlays.map { Activate(it) }
                commands
            }
            else -> emptyList()
        }

    private fun <C : Parcelable> BackStack<C>.overlayDiff(oldStack: BackStack<C>): List<ConfigurationCommand<C>> {
        val commands = mutableListOf<ConfigurationCommand<C>>()

        forEachIndexed { index, newElement ->
            val oldElement = oldStack.getOrNull(index)
            commands += oldElement?.removeNecessaryOverlays(newElement) ?: emptyList()
            commands += newElement.addNecessaryOverlays(oldElement)
        }

        return commands
    }

    /**
     * Old -> New
     *
     * Compares an old back stack element to a new back stack element and reflects changes.
     *
     * Difference will be removed from Old.
     */
    private fun <C : Parcelable> RoutingHistoryElement<C>.removeNecessaryOverlays(
        newElement: RoutingHistoryElement<C>
    ): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { index, oldElement ->
                if (oldElement == newElement.overlayAt(index)) emptyList()
                else listOf(
                    Deactivate(oldElement),
                    Remove(oldElement)
                )
            }
            .reversed()
            .flatten()

    /**
     * New -> Old
     *
     * Compares a new back stack element to an old back stack element and reflects changes.
     *
     * Difference will be added to New.
     */
    private fun <C : Parcelable> RoutingHistoryElement<C>.addNecessaryOverlays(
        oldElement: RoutingHistoryElement<C>?
    ): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { index, newElement ->
                if (newElement == oldElement?.overlayAt(index)) emptyList()
                else listOf(
                    Add(newElement),
                    Activate(newElement)
                )
            }
            .flatten()

    private fun <C : Parcelable> RoutingHistoryElement<C>.overlayAt(index: Int): Routing<C>? =
        overlays.getOrNull(index)
}

