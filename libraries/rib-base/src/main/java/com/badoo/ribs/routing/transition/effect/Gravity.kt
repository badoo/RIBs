package com.badoo.ribs.routing.transition.effect

enum class Gravity {
    LEFT, RIGHT, TOP, BOTTOM;

    fun reverse(): Gravity = when (this) {
        LEFT -> RIGHT
        RIGHT -> LEFT
        TOP -> BOTTOM
        BOTTOM -> TOP
    }
}
