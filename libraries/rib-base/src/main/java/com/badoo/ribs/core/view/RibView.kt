package com.badoo.ribs.core.view

import android.view.ViewGroup
import com.badoo.ribs.core.Node

interface RibView {

    val androidView: ViewGroup

    fun getParentViewForChild(child: Node<*>): ViewGroup? =
        androidView

    fun onChildViewAttached() {}
}
