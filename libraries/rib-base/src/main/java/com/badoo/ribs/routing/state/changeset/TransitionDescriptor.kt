package com.badoo.ribs.routing.state.changeset

import kotlin.random.Random

internal data class TransitionDescriptor(
    val id: Int = Random.nextInt(),
    val from: Any?,
    val to: Any?
) {
    fun isReverseOf(other: TransitionDescriptor) =
        from == other.to && to == other.from

    fun isContinuationOf(other: TransitionDescriptor) =
        from == other.to && to != other.from

    fun reverse(): TransitionDescriptor =
        TransitionDescriptor(
            from = to,
            to = from
        )

    companion object {
        val None: TransitionDescriptor =
            TransitionDescriptor(
                from = null,
                to = null
            )
    }
}
