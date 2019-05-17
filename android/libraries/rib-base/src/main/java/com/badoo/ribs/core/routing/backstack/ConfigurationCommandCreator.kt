package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Add
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Remove
import com.badoo.ribs.core.routing.backstack.ConfigurationKey.Content
import com.badoo.ribs.core.routing.backstack.ConfigurationKey.Overlay
import com.badoo.ribs.core.routing.backstack.ConfigurationKey.Overlay.Key
import com.badoo.ribs.core.routing.backstack.feature.BackStackElement
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature
import io.reactivex.Observable
import java.lang.Math.min

/**
 * Takes the state emissions from [BackStackFeature], and translates them to a stream of
 * [ConfigurationCommand]s.
 *
 * @see [ConfigurationCommandCreator.diff]
 */
internal fun <C : Parcelable> BackStackFeature<C>.commands(): Observable<ConfigurationCommand<C>> =
    Observable.wrap(this)
        .startWith(initialState) // Bootstrapper can overwrite it by the time we receive the first state emission here
        .buffer(2, 1)
        .map { ConfigurationCommandCreator.diff(it[0].backStack, it[1].backStack) }
        .flatMapIterable { items -> items }

internal object ConfigurationCommandCreator {

    /**
     * Calculates diff between two states of [BackStackFeature] (where each state contains the current list
     * of [C] configurations representing the back stack), and translates the diff to
     * a list of [ConfigurationCommand]s that represent getting from the old state to the new one.
     */
    fun <C : Parcelable> diff(
        oldStack: List<BackStackElement<C>>,
        newStack: List<BackStackElement<C>>
    ): List<ConfigurationCommand<C>> {

        if (newStack == oldStack) {
            return emptyList()
        }

        val commands = mutableListOf<ConfigurationCommand<C>>()

        // lazy way to check simple case when only an overlay changed in the last element
        if (newStack.isNotEmpty() &&
            oldStack.isNotEmpty() &&
            newStack.dropLast(1) == oldStack.dropLast(1) &&
            newStack.last().configuration == oldStack.last().configuration) {

            val contentKey = Content(newStack.size)
            val lastNew = newStack.last()
            val lastOld = oldStack.last()
            commands += lastOld.overlays
                .filterIndexed { overlayIndex, element -> lastNew.overlays.getOrNull(overlayIndex) != element }
                .mapIndexed { overlayIndex, _ -> listOf(
                        Deactivate<C>(Overlay(Key(contentKey, overlayIndex))),
                        Remove<C>(Overlay(Key(contentKey, overlayIndex)))
                    )
                }.flatten()

            commands += lastNew.overlays
                .filterIndexed { overlayIndex, element -> lastOld.overlays.getOrNull(overlayIndex) != element }
                .mapIndexed { overlayIndex, element -> listOf(
                        Add(Overlay(Key(contentKey, overlayIndex)), element.configuration),
                        Activate<C>(Overlay(Key(contentKey, overlayIndex)))
                    )
                }.flatten()

            return commands
        }

        val indexOfLastCommonElement = findIndexOfLastCommonElement(oldStack, newStack)
        commands += oldStack.deactivateIfNeeded()
        commands += oldStack.removeUntil(indexOfLastCommonElement)
        commands += newStack.addFrom(indexOfLastCommonElement)
        commands += newStack.activateIfNeeded()

        return commands
    }

    private fun findIndexOfLastCommonElement(
        oldStack: List<BackStackElement<*>>,
        newStack: List<BackStackElement<*>>
    ): Int {
        var idx = -1

        for (i in 0..min(oldStack.lastIndex, newStack.lastIndex)) {
            // Compare only configurations, as if it's only the overlays that are different, they will be handled later
            if (newStack.elementAt(i).configuration != oldStack.elementAt(i).configuration) {
                return idx
            }

            idx = i
        }

        return idx
    }

    private fun <C : Parcelable> List<BackStackElement<C>>.deactivateIfNeeded(): List<ConfigurationCommand<C>> =
        when {
            isNotEmpty() -> {
                val contentKey = Content(lastIndex)
                val commands = mutableListOf<ConfigurationCommand<C>>()
                commands += last().deactivateAllOverlays(contentKey)
                commands += Deactivate(contentKey)
                commands
            }
            else -> emptyList()
        }

    private fun <C : Parcelable> List<BackStackElement<C>>.removeUntil(targetIdxExclusive: Int): List<ConfigurationCommand<C>> {
        val offset = targetIdxExclusive + 1
        return subList(offset, size)
            .mapIndexed { contentIndex, backStackElement ->
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val contentKey = Content(offset + contentIndex)
                commands += Remove(contentKey)
                commands += backStackElement.removeAllOverlays(contentKey)
                commands += backStackElement.deactivateAllOverlays(contentKey)
                commands
            }
            .asReversed()
            .flatten()
    }

    private fun <C : Parcelable> List<BackStackElement<C>>.addFrom(targetIdxExclusive: Int): List<ConfigurationCommand<C>> {
        val offset = targetIdxExclusive + 1
        return subList(offset, size)
            .mapIndexed { contentIndex, backStackElement ->
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val contentKey = Content(offset + contentIndex)
                commands += Add(contentKey, backStackElement.configuration)
                commands += backStackElement.addAllOverlays(contentKey)
                commands
            }
            .flatten()
    }

    private fun <C : Parcelable> List<BackStackElement<C>>.activateIfNeeded(): List<ConfigurationCommand<C>> =
        when {
            isNotEmpty() -> {
                val commands = mutableListOf<ConfigurationCommand<C>>()
                val contentKey = Content(lastIndex)
                commands += Activate(contentKey)
                commands += last().activateAllOverlays(contentKey)
                commands
            }
            else -> emptyList()
        }

    private fun <C : Parcelable> BackStackElement<C>.addAllOverlays(content: Content): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { overlayIndex, backStackElement ->
                Add(Overlay(Key(content, overlayIndex)), backStackElement.configuration)
            }

    private fun <C : Parcelable> BackStackElement<C>.activateAllOverlays(contentKey: Content): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { overlayIndex, _ ->
                Activate<C>(Overlay(Key(contentKey, overlayIndex)))
            }

    private fun <C : Parcelable> BackStackElement<C>.deactivateAllOverlays(contentKey: Content): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { overlayIndex, _ ->
                Deactivate<C>(Overlay(Key(contentKey, overlayIndex)))
            }
            .asReversed()

    private fun <C : Parcelable> BackStackElement<C>.removeAllOverlays(contentKey: Content): List<ConfigurationCommand<C>> =
        overlays
            .mapIndexed { overlayIndex, _ ->
                Remove<C>(Overlay(Key(contentKey, overlayIndex)))
            }
            .asReversed()
}
