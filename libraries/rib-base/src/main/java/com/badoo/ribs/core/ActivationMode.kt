package com.badoo.ribs.core

/**
 * Describes the way a new child will need to be activated.
 *
 * Activation != logical parenting. A child that is added to its logical parent (e.g. for receiving
 * lifecycle callbacks) might or might not live somewhere else in the view tree.
 */
enum class ActivationMode {
    /**
     * The default case.
     *
     * The child's view will be attached/detached automatically to the parent.
     *
     * No action is required by client code.
     */
    ATTACH_TO_PARENT,

    /**
     * The child's view is somewhere else in the view tree and it shouldn't be added to the parent's
     * view automatically.
     *
     * Action is required by client code to handle activation (i.e. implement view attach / detach to custom
     * parentViewGroup).
     *
     * Example: hosting in RecyclerView, where parent view is in a ViewHolder.
     */
    CLIENT,

    /**
     * The child's view is somewhere else in the view tree and it shouldn't be added to the parent's
     * view automatically.
     *
     * No action is required by client code. View will be attached/detached
     * automatically by the routing action itself upon execution.
     *
     * Example: dialog routing action
     */
    BY_ROUTING_ACTION
}
