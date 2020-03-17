package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Activate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.ConfigurationCommandCreator.diff
import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Content
import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Overlay
import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Overlay.Key
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeatureState
import com.badoo.ribs.core.routing.configuration.feature.TransitionDescriptor
import io.reactivex.Observable
import java.lang.Math.min

/**
 * Takes the state emissions from [BackStackFeature], and translates them to a stream of
 * [ConfigurationCommand]s.
 *
 * @see [ConfigurationCommandCreator.diff]
 */
internal fun <C : Parcelable> BackStackFeature<C>.toCommands(): Observable<Transaction<C>> =
    Observable.wrap(this)
        .startWith(
            listOf(
                BackStackFeatureState(backStack = emptyList()),
                initialState // Bootstrapper can overwrite it by the time we receive the first state emission here
            )
        )
        .buffer(2, 1)
        .map { (previous, current) ->
            Transaction.ListOfCommands(
                descriptor = TransitionDescriptor(from = previous, to = current),
                commands = diff(previous.backStack, current.backStack)
            )
        }

internal object ConfigurationCommandCreator {

    /**
     * Calculates diff between two states of [BackStackFeature] (where each state contains the current list
     * of [C] configurations representing the back stack), and translates the diff to
     * a list of [ConfigurationCommand]s that represent getting from the old state to the new one.
     */
    fun <C : Parcelable> diff(
        oldStack: List<BackStackElement<C>>,
        newStack: List<BackStackElement<C>>
    ): List<ConfigurationCommand<C>> =
        when (newStack) {
            oldStack -> emptyList()
            else -> {
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val indexOfLastCommonConfiguration = findIndexOfLastCommonConfiguration(oldStack, newStack)
                commands += oldStack.deactivateIfNeeded(newStack)
                commands += oldStack.removeUntil(indexOfLastCommonConfiguration)
                commands += newStack.addFrom(indexOfLastCommonConfiguration)
                commands += newStack.activateIfNeeded(oldStack)
                commands += newStack.overlayDiff(oldStack)
                commands
        }
    }

    private fun findIndexOfLastCommonConfiguration(
        oldStack: List<BackStackElement<*>>,
        newStack: List<BackStackElement<*>>
    ): Int {
        var idx = -1
        val lastCommonIndex = min(oldStack.lastIndex, newStack.lastIndex)

        for (i in 0..lastCommonIndex) {
            // Compare only configurations, as if it's only the overlays that are different, they will be handled later
            if (newStack.elementAt(i).configuration != oldStack.elementAt(i).configuration) {
                return idx
            }

            idx = i
        }

        return idx
    }

    private fun <C : Parcelable> List<BackStackElement<C>>.deactivateIfNeeded(other: List<BackStackElement<C>>): List<ConfigurationCommand<C>> =
        when {
            contentListHasChanged(other) && isNotEmpty() -> {
                val configuration = last().configuration
                val contentKey = Content(lastIndex, configuration)
                val commands = mutableListOf<ConfigurationCommand<C>>()
                commands += last().deactivateAllOverlays(contentKey)
                commands += Deactivate(contentKey, configuration)
                commands
            }
            else -> emptyList()
        }

    /**
     * A comparison between two lists that doesn't care about overlay differences, only the direct elements
     */
    private fun <C : Parcelable> List<BackStackElement<C>>.contentListHasChanged(other: List<BackStackElement<C>>) =
        map { it.configuration } != other.map { it.configuration }

    private fun <C : Parcelable> List<BackStackElement<C>>.removeUntil(targetIdxExclusive: Int): List<ConfigurationCommand<C>> {
        val offset = targetIdxExclusive + 1
        return subList(offset, size)
            .mapIndexed { contentIndex, backStackElement ->
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val realIndex = offset + contentIndex
                val contentKey = Content(realIndex, backStackElement.configuration)
                commands += Remove(contentKey, backStackElement.configuration)
                commands += backStackElement.removeAllOverlays(contentKey).reversed()
                if (realIndex != lastIndex) {
                    commands += backStackElement.deactivateAllOverlays(contentKey).reversed()
                }

                commands
            }
            .flatten()
            .reversed()

    }

    private fun <C : Parcelable> List<BackStackElement<C>>.addFrom(targetIdxExclusive: Int): List<ConfigurationCommand<C>> {
        val offset = targetIdxExclusive + 1
        return subList(offset, size)
            .mapIndexed { contentIndex, backStackElement ->
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val contentKey = Content(offset + contentIndex, backStackElement.configuration)
                commands += Add(contentKey, backStackElement.configuration)
                commands += backStackElement.addAllOverlays(contentKey)
                commands
            }
            .flatten()
    }

    private fun <C : Parcelable> List<BackStackElement<C>>.activateIfNeeded(other: List<BackStackElement<C>>): List<ConfigurationCommand<C>> =
        when {
            isNotEmpty() && contentListHasChanged(other) -> {
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val configuration = last().configuration
                val contentKey = Content(lastIndex, configuration)
                val activate = Activate<C>(contentKey, configuration)
                commands += activate
                commands += last().activateAllOverlays(contentKey)
                commands
            }
            else -> emptyList()
        }

    private fun <C : Parcelable> BackStackElement<C>.addAllOverlays(content: Content): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { overlayIndex, overlayConfiguration ->
                Add(Overlay(Key(content, overlayIndex, overlayConfiguration)), overlayConfiguration)
            }

    private fun <C : Parcelable> BackStackElement<C>.activateAllOverlays(contentKey: Content): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { overlayIndex, overlayConfiguration ->
                Activate<C>(Overlay(Key(contentKey, overlayIndex, overlayConfiguration)), overlayConfiguration)
            }

    private fun <C : Parcelable> BackStackElement<C>.deactivateAllOverlays(contentKey: Content): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { overlayIndex, overlayConfiguration ->
                Deactivate<C>(Overlay(Key(contentKey, overlayIndex, overlayConfiguration)), overlayConfiguration)
            }
            .reversed()

    private fun <C : Parcelable> BackStackElement<C>.removeAllOverlays(contentKey: Content): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { overlayIndex, overlayConfiguration ->
                Remove<C>(Overlay(Key(contentKey, overlayIndex, overlayConfiguration)), overlayConfiguration)
            }
            .reversed()

    private fun <C : Parcelable> List<BackStackElement<C>>.overlayDiff(oldStack: List<BackStackElement<C>>): List<ConfigurationCommand<C>> {
        val commands = mutableListOf<ConfigurationCommand<C>>()

        forEachIndexed { index, newElement ->
            val oldElement = oldStack.getOrNull(index)
            val contentKey = Content(index, newElement.configuration)
            commands += oldElement?.removeNecessaryOverlays(newElement, contentKey) ?: emptyList()
            commands += newElement.addNecessaryOverlays(oldElement, contentKey)
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
    private fun <C : Parcelable> BackStackElement<C>.removeNecessaryOverlays(
        newElement: BackStackElement<C>,
        contentKey: Content
    ): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { index, oldElement ->
                if (oldElement == newElement.overlayAt(index)) emptyList()
                else listOf(
                    Deactivate<C>(Overlay(Key(contentKey, index, oldElement)), oldElement),
                    Remove<C>(Overlay(Key(contentKey, index, oldElement)), oldElement)
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
    private fun <C : Parcelable> BackStackElement<C>.addNecessaryOverlays(
        oldElement: BackStackElement<C>?,
        contentKey: Content
    ): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { index, newElement ->
                if (newElement == oldElement?.overlayAt(index)) emptyList()
                else listOf(
                    Add(Overlay(Key(contentKey, index, newElement)), newElement),
                    Activate<C>(Overlay(Key(contentKey, index, newElement)), newElement)
                )
            }
            .flatten()

    private fun <C : Parcelable> BackStackElement<C>.overlayAt(index: Int): C? =
        overlays.getOrNull(index)
}
