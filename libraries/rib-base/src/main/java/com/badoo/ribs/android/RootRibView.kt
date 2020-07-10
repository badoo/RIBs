package com.badoo.ribs.android

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView

class AndroidRootHost(
    host: ViewGroup
) : AndroidRibView() {

    override val androidView: ViewGroup =
        host
}

fun ViewGroup.attach(node: Node<*>) {
    node.onCreateView(this)
    node.view?.let { this.addView(it.androidView) }
    node.onAttachToView()
}

fun ViewGroup.detach(node: Node<*>) {
    node.view?.let { this.removeView(it.androidView) }
    node.onDetachFromView()
}
