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

interface ViewAware<V : RibView> : Plugin {
    fun onViewCreated(view: V, viewLifecycle: Lifecycle) {}
}

interface RibLifecycleAware : Plugin {
    fun onAttach(ribLifecycle: Lifecycle) {}

    fun onAttachToView(parentViewGroup: ViewGroup) {}

    fun onDetachFromView(parentViewGroup: ViewGroup) {}

    fun onDetach() {}
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

interface BackPressHandler : Plugin {
    // TODO this makes sense when we have a subtree, but how to simplify it if we don't?

    fun handleBackPressBeforeDownstream(): Boolean =
        false

    fun handleBackPressAfterDownstream(): Boolean =
        false
}
