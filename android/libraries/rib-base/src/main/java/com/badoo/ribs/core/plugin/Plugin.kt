package com.badoo.ribs.core.plugin

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

interface Plugin

interface NodeAware : Plugin {
    fun init(node: Node<*>) {}
}

interface NodeLifecycleAware : Plugin {
    fun onAttach(nodeLifecycle: Lifecycle) {}

    fun onDetach() {}
}

interface ViewAware<V : RibView> : Plugin {
    fun onViewCreated(view: V, viewLifecycle: Lifecycle) {}
}

interface ViewLifecycleAware : Plugin {
    fun onAttachToView(parentViewGroup: ViewGroup) {}

    fun onDetachFromView(parentViewGroup: ViewGroup) {}
}

interface SubtreeChangeAware : Plugin {
    fun onAttachChildNode(child: Node<*>) {}

    fun onDetachChildNode(child: Node<*>) {}
}

interface SubtreeViewChangeAware : Plugin {
    fun onAttachChildView(child: Node<*>) {}

    fun onDetachChildView(child: Node<*>) {}
}

interface AndroidLifecycleAware : Plugin {
    fun onStart() {}

    fun onStop() {}

    fun onResume() {}

    fun onPause() {}
}

interface SavesInstanceState : Plugin {
    fun onSaveInstanceState(outState: Bundle) {}
}

interface SystemAware : Plugin {
    fun onLowMemory() {}
}

/**
 * PriorityBackPressHandler can react to back press events based on the order Node is dispatching
 * then.
 *
 * Implement this interface if you want to add multiple business-logic based back press handlers
 * to a Node and guarantee the order they can react.
 *
 * @see BackPressHandler for most common case.
 */
interface PriorityBackPressHandler : Plugin {

    /**
     * HIGH, NORMAL, and LOW are the levels you most probably want to react to in your business logic.
     *
     * They are triggered after Node already asked its subtree and the whole subtree didn't handle the event.
     *
     * How you make sense of them is up to your interpretation. Default functionality in the
     * framework doesn't implement these levels.
     */
    enum class Priority {
        HIGH,
        NORMAL,
        LOW
    }

    fun handleBackPress(priority: Priority): Boolean =
        false
}

/**
 * A simplified version of PriorityBackPressHandler which only reacts to Priority level NORMAL.
 *
 * If you don't have any complex logic or multiple BackPressHandler instances, this is probably
 * the interface you want to implement.
 */
interface BackPressHandler : PriorityBackPressHandler {

    override fun handleBackPress(priority: PriorityBackPressHandler.Priority): Boolean =
        when (priority) {
            PriorityBackPressHandler.Priority.NORMAL -> handleBackPress()
            else -> false
        }

    fun handleBackPress(): Boolean =
        false
}

/**
 * SubtreeBackPressHandler is used only for purposes of routing mechanisms.
 *
 * It will be asked twice by the framework: first, before everything else, and last, after everything else.
 *
 * In everyday cases you probably don't want to implement this interface. If you do, make sure
 * you understand the different priority levels before using them.
 *
 * @see BackPressHandler for most common case.
 * @see PriorityBackPressHandler for regular cases.
 */
interface SubtreeBackPressHandler : Plugin {
    enum class Priority {
        /**
         * This level is dispatched first by Node, even before asking child Nodes in the subtree.
         *
         * Routing can use this to pop overlays as top priority.
         *
         * Important:
         * In almost any other case you probably DON'T want to react on this level, otherwise
         * your business logic on the highest levels will take precedence before your active leaf
         * nodes. In a typical setup this means your application quits before on-screen nodes
         * even have a chance to react.
         */
        FIRST,

        /**
         * This level is dispatched last by Node if all above priority levels failed to handle
         * a back press.
         *
         * Routing can use this to e.g. pop content back stack.
         */
        FALLBACK
    }

    fun handleBackPress(priority: Priority): Boolean =
        false
}
