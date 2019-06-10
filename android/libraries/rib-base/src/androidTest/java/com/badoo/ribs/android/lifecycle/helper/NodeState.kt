package com.badoo.ribs.android.lifecycle.helper

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Lifecycle.State.*

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
            viewLifeCycleState = DESTROYED
        )
        val DETACHED = NodeState(
            attached = false,
            viewAttached = false,
            ribLifeCycleState = DESTROYED,
            viewLifeCycleState = DESTROYED
        )
    }

    override fun toString(): String =
        when {
            attached && viewAttached -> {
                when {
                    ribLifeCycleState == RESUMED && viewLifeCycleState == RESUMED -> "ON_SCREEN"
                    ribLifeCycleState == STARTED && viewLifeCycleState == STARTED -> "ON_SCREEN_PAUSED"
                    ribLifeCycleState == CREATED && viewLifeCycleState == CREATED -> "ON_SCREEN_STOPPED"
                    else -> "ON_SCREEN [!INVALID!] (rib: $ribLifeCycleState / view: $viewLifeCycleState)"
                }
            }

            attached && !viewAttached -> {
                when {
                    ribLifeCycleState == CREATED && viewLifeCycleState == DESTROYED -> "VIEW_DETACHED"
                    else -> "VIEW_DETACHED [!INVALID!] (rib: $ribLifeCycleState / view: $viewLifeCycleState)"
                }
            }

            !attached && !viewAttached -> {
                when {
                    ribLifeCycleState == DESTROYED && viewLifeCycleState == DESTROYED -> "DETACHED"
                    else -> "DETACHED [!INVALID!] (rib: $ribLifeCycleState / view: $viewLifeCycleState)"
                }
            }
            else -> "!!! INVALID - ${super.toString()} !!!"
        }
}
