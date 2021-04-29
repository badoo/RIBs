package com.badoo.ribs.android.lifecycle.helper

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.Lifecycle.State.STARTED

data class NodeState(
    val attached: Boolean,
    val viewAttached: Boolean,
    val ribLifeCycleState: Lifecycle.State,
    val viewLifeCycleState: Lifecycle.State?
) {
    companion object {
        val ON_SCREEN = NodeState(
            attached = true,
            viewAttached = true,
            ribLifeCycleState = RESUMED,
            viewLifeCycleState = RESUMED
        )
        val ON_SCREEN_PAUSED = NodeState(
            attached = true,
            viewAttached = true,
            ribLifeCycleState = STARTED,
            viewLifeCycleState = STARTED
        )
        val ON_SCREEN_STOPPED = NodeState(
            attached = true,
            viewAttached = true,
            ribLifeCycleState = CREATED,
            viewLifeCycleState = CREATED
        )
        val VIEW_DETACHED = NodeState(
            attached = true,
            viewAttached = false,
            ribLifeCycleState = CREATED,
            viewLifeCycleState = null
        )
        val DETACHED = NodeState(
            attached = false,
            viewAttached = false,
            ribLifeCycleState = DESTROYED,
            viewLifeCycleState = null
        )
        val ON_SCREEN_RESUMED = NodeState(
            attached = true,
            viewAttached = true,
            ribLifeCycleState = CREATED,
            viewLifeCycleState = RESUMED
        )
    }

    override fun toString(): String = when {
        attached && viewAttached -> onScreenToString()
        attached && !viewAttached -> viewDetachedToString()
        !attached && !viewAttached -> detachedToString()
        else -> "!!! INVALID - ${super.toString()} !!!"
    }

    private fun onScreenToString(): String = when {
        ribLifeCycleState == RESUMED && viewLifeCycleState == RESUMED -> "ON_SCREEN"
        ribLifeCycleState == STARTED && viewLifeCycleState == STARTED -> "ON_SCREEN_PAUSED"
        ribLifeCycleState == CREATED && viewLifeCycleState == CREATED -> "ON_SCREEN_STOPPED"
        else -> "ON_SCREEN [!INVALID!] (rib: $ribLifeCycleState / view: $viewLifeCycleState)"
    }

    private fun viewDetachedToString(): String = when {
        ribLifeCycleState == CREATED && (viewLifeCycleState == null || viewLifeCycleState == DESTROYED) -> "VIEW_DETACHED"
        else -> "VIEW_DETACHED [!INVALID!] (rib: $ribLifeCycleState / view: $viewLifeCycleState)"
    }

    private fun detachedToString(): String = when {
        ribLifeCycleState == DESTROYED && (viewLifeCycleState == null || viewLifeCycleState == DESTROYED) -> "DETACHED"
        else -> "DETACHED [!INVALID!] (rib: $ribLifeCycleState / view: $viewLifeCycleState)"
    }
}
