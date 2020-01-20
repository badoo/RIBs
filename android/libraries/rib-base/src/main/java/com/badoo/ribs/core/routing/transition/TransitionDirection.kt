package com.badoo.ribs.core.routing.transition

sealed class TransitionDirection {
    object Enter : TransitionDirection()
    object Exit : TransitionDirection()
}
