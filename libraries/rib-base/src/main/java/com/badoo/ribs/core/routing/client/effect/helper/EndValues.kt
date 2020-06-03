package com.badoo.ribs.core.routing.client.effect.helper

data class EndValues(
    val from: Float,
    val to: Float
) {
    val difference = to - from

    fun reverse() =
        EndValues(
            from = to,
            to = from
        )
}
