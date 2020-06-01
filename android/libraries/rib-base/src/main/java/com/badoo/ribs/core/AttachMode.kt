package com.badoo.ribs.core

@ExperimentalApi
// TODO consider if this is better as internal
enum class AttachMode { // TODO rename --> Activation
    /**
     * The node's view will be attached/detached automatically to its parent.
     *
     * No action is required by client code.
     */
    PARENT, // TODO rename --> AUTOMATIC or DEFAULT

    /**
     * The node's view is somewhere else in the view tree.
     *
     * Action is required by client code to implement view attach / detach.
     *
     * Example: hosting in RecyclerView, where parent view is in a ViewHolder.
     */
    EXTERNAL, // TODO rename --> CLIENT

    /**
     * The node's view is somewhere else in the view tree.
     *
     * No action is required by client code. View will be attached/detached
     * automatically by the routing action itself upon execution.
     *
     * Example: dialog routing action
     */
    REMOTE // // TODO rename --> ROUTING_ACTION
}
