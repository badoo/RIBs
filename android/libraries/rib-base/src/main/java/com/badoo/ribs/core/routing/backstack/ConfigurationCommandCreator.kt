package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.routing.backstack.BackStackFeature.State
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Add
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Deactivate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Remove
import java.lang.Math.min

internal class ConfigurationCommandCreator<C : Parcelable> :
        (State<C>, State<C>) -> List<ConfigurationCommand<C>> {

    override fun invoke(oldState: State<C>, newState: State<C>): List<ConfigurationCommand<C>> {
        if (newState == oldState) {
            return emptyList()
        }

        val indexOfLastCommonElement = findIndexOfLastCommonElement(oldState.backStack, newState.backStack)
        val commands = mutableListOf<ConfigurationCommand<C>>()
        commands += oldState.backStack.makePassiveIfNeeded()
        commands += oldState.backStack.removeUntil(indexOfLastCommonElement)
        commands += newState.backStack.addFrom(indexOfLastCommonElement)
        commands += newState.backStack.makeActiveIfNeeded()

        return commands
    }

    private fun findIndexOfLastCommonElement(oldStack: List<C>, newStack: List<C>): Int {
        var idx = -1

        for (i in 0..min(oldStack.lastIndex, newStack.lastIndex)) {
            if (newStack.elementAt(i) != oldStack.elementAt(i)) {
                return idx
            }

            idx = i
        }

        return idx
    }

    private fun List<C>.makePassiveIfNeeded(): List<ConfigurationCommand<C>> =
        when {
            lastIndex > -1 -> listOf(Deactivate(lastIndex))
            else -> emptyList()
        }

    private fun List<C>.removeUntil(targetIdxExclusive: Int): List<ConfigurationCommand<C>> {
        val offset = targetIdxExclusive + 1
        return subList(offset, size)
            .mapIndexed { index, _ -> Remove<C>(offset + index) }
            .asReversed()
    }

    private fun List<C>.addFrom(targetIdxExclusive: Int): List<ConfigurationCommand<C>> {
        val offset = targetIdxExclusive + 1
        return subList(offset, size)
            .mapIndexed { index, configuration -> Add(offset + index, configuration) }
    }

    private fun List<C>.makeActiveIfNeeded(): List<ConfigurationCommand<C>> =
        when {
            lastIndex > -1 -> listOf(Activate(lastIndex))
            else -> emptyList()
        }
}
