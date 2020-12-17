package com.badoo.ribs.core.view

import android.content.Context
import android.view.ViewGroup
import com.badoo.ribs.core.Node

interface RibView {

    val androidView: ViewGroup

    val context: Context
        get() = androidView.context

    /**
     * Shortcut, see overloaded version
     */
    fun attachChild(child: Node<*>) {
        attachChild(child, child)
    }

    /**
     * Attach a child to this view.
     *
     * @param child The actual child to attach. Might be deeper in the tree if intermediary steps
     * don't have their own view to attach it to.
     * @param subtreeOf The Node to do checks on when deciding where to attach [child]. This allows
     * the current view to stay decoupled from [child] but still attach any descendants of [subtreeOf]
     * correctly, without having to care whether [subtreeOf] (and any other intermediary Node) has
     * its own view or not.
     */
    fun attachChild(child: Node<*>, subtreeOf: Node<*>)

    /**
     * Shortcut, see overloaded version
     */
    fun detachChild(child: Node<*>) {
        detachChild(child, child)
    }

    /**
     * Detaches a child from this view. Acts as the opposite of [attachChild].
     */
    fun detachChild(child: Node<*>, subtreeOf: Node<*>)
}

