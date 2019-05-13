package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.routing.backstack.BackStackManager.State
import com.badoo.ribs.core.routing.backstack.ConnectorCommand.*
import java.lang.Math.min

internal class ConnectorCommandCreator<C : Parcelable> :
        (State<C>, State<C>) -> List<ConnectorCommand<C>> {

    override fun invoke(oldState: State<C>, newState: State<C>): List<ConnectorCommand<C>> {
        if (newState == oldState) {
            return emptyList()
        }

        val indexOfLastCommonElement = findIndexOfLastCommonElement(oldState.backStack, newState.backStack)
        val commands = mutableListOf<ConnectorCommand<C>>()
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

    private fun List<C>.makePassiveIfNeeded(): List<ConnectorCommand<C>> =
        when {
            lastIndex > -1 -> listOf(MakePassive(lastIndex))
            else -> emptyList()
        }

    private fun List<C>.removeUntil(targetIdxExclusive: Int): List<ConnectorCommand<C>> {
        val offset = targetIdxExclusive + 1
        return subList(offset, size)
            .mapIndexed { index, _ -> Remove<C>(offset + index) }
            .asReversed()
    }

    private fun List<C>.addFrom(targetIdxExclusive: Int): List<ConnectorCommand<C>> {
        val offset = targetIdxExclusive + 1
        return subList(offset, size)
            .mapIndexed { index, configuration -> Add(offset + index, configuration) }
    }

    private fun List<C>.makeActiveIfNeeded(): List<ConnectorCommand<C>> =
        when {
            lastIndex > -1 -> listOf(MakeActive(lastIndex))
            else -> emptyList()
        }
}
