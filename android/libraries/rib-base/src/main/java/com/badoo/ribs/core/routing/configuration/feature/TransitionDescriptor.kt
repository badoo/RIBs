package com.badoo.ribs.core.routing.configuration.feature

data class TransitionDescriptor(
    val from: Any?,
    val to: Any?
) {
    fun isReverseOf(other: TransitionDescriptor) =
        from == other.to && to == other.from // todo only match exiting

    fun isContinuationOf(other: TransitionDescriptor) =
        from == other.to && to != other.from // todo only match entering

    companion object {
        val None =
            TransitionDescriptor(
                null,
                null
            )
    }
}
