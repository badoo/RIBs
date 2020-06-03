package com.badoo.ribs.core.routing.client.effect

enum class Gravity {
    LEFT, RIGHT, TOP, BOTTOM;

    fun reverse(): Gravity = when (this) {
        LEFT -> RIGHT
        RIGHT -> LEFT
        TOP -> BOTTOM
        BOTTOM -> TOP
    }
}
