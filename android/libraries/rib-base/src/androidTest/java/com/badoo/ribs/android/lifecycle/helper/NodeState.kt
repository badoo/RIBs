package com.badoo.ribs.android.lifecycle.helper

import android.arch.lifecycle.Lifecycle

data class NodeState(
    val attached: Boolean,
    val viewAttached: Boolean,
    val ribLifeCycleState: Lifecycle.State,
    val viewLifeCycleState: Lifecycle.State
) {
    companion object {
        val ON_SCREEN = NodeState(
            attached = true,
            viewAttached = true,
            ribLifeCycleState = Lifecycle.State.RESUMED,
            viewLifeCycleState = Lifecycle.State.RESUMED
        )
        val VIEW_DETACHED = NodeState(
            attached = true,
            viewAttached = false,
            ribLifeCycleState = Lifecycle.State.CREATED,
            viewLifeCycleState = Lifecycle.State.DESTROYED
        )
        val DETACHED = NodeState(
            attached = false,
            viewAttached = false,
            ribLifeCycleState = Lifecycle.State.DESTROYED,
            viewLifeCycleState = Lifecycle.State.DESTROYED
        )
    }

    override fun toString(): String =
        when {
            attached && viewAttached -> "ON_SCREEN (rib: $ribLifeCycleState / view: $viewLifeCycleState)"
            attached && !viewAttached -> "VIEW_DETACHED (rib: $ribLifeCycleState / view: $viewLifeCycleState)"
            !attached && !viewAttached -> "DETACHED (rib: $ribLifeCycleState / view: $viewLifeCycleState)"
            else -> "!!! INVALID !!!"
        }
}
