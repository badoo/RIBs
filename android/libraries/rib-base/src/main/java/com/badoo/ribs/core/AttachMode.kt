package com.badoo.ribs.core

enum class AttachMode {
    /**
     * The node's view attach/detach is managed by its parent.
     */
    PARENT,

    /**
     * The node's view is somewhere else in the view tree, and it should not be managed
     *  by its parent.
     *
     * Examples can be: the child's view is hosted in a dialog, or added to some other
     *  generic host node.
     */
    EXTERNAL
}
