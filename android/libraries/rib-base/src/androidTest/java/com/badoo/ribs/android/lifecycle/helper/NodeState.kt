package com.badoo.ribs.android.lifecycle.helper

data class NodeState(
    val attached: Boolean,
    val viewAttached: Boolean
) {
    companion object {
        val ON_SCREEN = NodeState(attached = true, viewAttached = true)
        val VIEW_DETACHED = NodeState(attached = true, viewAttached = false)
        val DETACHED = NodeState(attached = false, viewAttached = false)
    }

    override fun toString(): String =
        when {
            attached && viewAttached -> "ON_SCREEN"
            attached && !viewAttached -> "VIEW_DETACHED"
            !attached && !viewAttached -> "DETACHED"
            else -> "!!! INVALID !!!"
        }
}
