package com.badoo.ribs.core.plugin

import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView

interface Plugin

interface RibAware<T : Rib> : Plugin {
    val rib: T

    fun init(rib: T) {}
}

interface NodeAware : Plugin {
    val node: Node<*>

    fun init(node: Node<*>) {}
}

interface NodeLifecycleAware : Plugin {
    fun onCreate() {}

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
    fun onChildCreated(child: Node<*>) {}

    fun onAttachChild(child: Node<*>) {}

    fun onDetachChild(child: Node<*>) {}
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
