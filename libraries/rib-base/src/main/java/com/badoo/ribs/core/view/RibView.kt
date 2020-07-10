package com.badoo.ribs.core.view

import android.view.ViewGroup
import com.badoo.ribs.core.Node

interface RibView {

    val androidView: ViewGroup

    fun attachChild(child: Node<*>) // TODO consider Rib instead?

    fun detachChild(child: Node<*>) // TODO consider Rib instead?
}

