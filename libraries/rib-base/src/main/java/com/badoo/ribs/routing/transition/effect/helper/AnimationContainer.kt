package com.badoo.ribs.routing.transition.effect.helper

import android.view.View
import android.view.ViewGroup

sealed class AnimationContainer {
    object RootView : AnimationContainer()
    object Parent : AnimationContainer()
    data class FindParentById(val id: Int) : AnimationContainer()
}


fun View.findParentById(parentId: Int): ViewGroup? {
    var current: ViewGroup? = this.parent as ViewGroup?
    while (current != null && current.id != parentId) {
        current = current.parent as ViewGroup?
    }

    return current
}
