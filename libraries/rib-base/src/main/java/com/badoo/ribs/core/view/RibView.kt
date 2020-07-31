package com.badoo.ribs.core.view

import android.content.Context
import android.view.ViewGroup
import com.badoo.ribs.core.Node

interface RibView {

    val androidView: ViewGroup

    val context: Context
        get() =  androidView.context

    fun attachChild(child: Node<*>) // TODO consider Rib instead?

    fun detachChild(child: Node<*>) // TODO consider Rib instead?
}

