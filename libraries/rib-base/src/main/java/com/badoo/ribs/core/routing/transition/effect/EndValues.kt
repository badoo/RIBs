package com.badoo.ribs.core.routing.transition.effect

data class EndValues(
    val from: Float,
    val to: Float
) {
    val difference = to - from

    fun reverse() = EndValues(
        from = to,
        to = from
    )
}
