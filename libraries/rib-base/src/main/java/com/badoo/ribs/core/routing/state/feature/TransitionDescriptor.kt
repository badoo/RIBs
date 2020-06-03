package com.badoo.ribs.core.routing.state.feature

internal data class TransitionDescriptor(
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
        val None =
            TransitionDescriptor(
                null,
                null
            )
    }
}
