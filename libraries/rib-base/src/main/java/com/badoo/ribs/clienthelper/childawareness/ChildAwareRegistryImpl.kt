package com.badoo.ribs.clienthelper.childawareness

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.clienthelper.childawareness.ChildAwareCallbackInfo.Mode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

class ChildAwareRegistryImpl : ChildAwareRegistry {

    private val callbacks: MutableList<ChildAwareCallbackInfo> = ArrayList()

    override lateinit var node: Node<*>
        private set

    override fun init(node: Node<*>) {
        this.node = node
    }

    override fun <T : Rib> whenChildBuilt(
        lifecycle: Lifecycle,
        child: KClass<T>,
        callback: ChildCallback<T>
    ) {
        val info = ChildAwareCallbackInfo.Single(child, callback, lifecycle, Mode.ON_BUILT)
        callbacks += info
        notifyWhenRegistered(info)
    }

    override fun <T : Rib> whenChildAttached(
        lifecycle: Lifecycle,
        child: KClass<T>,
        callback: ChildCallback<T>
    ) {
        val info = ChildAwareCallbackInfo.Single(child, callback, lifecycle, Mode.ON_ATTACH)
        callbacks += info
        notifyWhenRegistered(info)
    }

    override fun <T1 : Rib, T2 : Rib> whenChildrenBuilt(
        lifecycle: Lifecycle,
        child1: KClass<T1>,
        child2: KClass<T2>,
        callback: ChildrenCallback<T1, T2>
    ) {
        val info =
            ChildAwareCallbackInfo.Double(child1, child2, callback, lifecycle, Mode.ON_BUILT)
        callbacks += info
        notifyWhenRegistered(info)
    }

    override fun <T : Rib, R : Rib> whenChildrenAttached(
        lifecycle: Lifecycle,
        child1: KClass<T>,
        child2: KClass<R>,
        callback: ChildrenCallback<T, R>,
    ) {
        val info =
            ChildAwareCallbackInfo.Double(child1, child2, callback, lifecycle, Mode.ON_ATTACH)
        callbacks += info
        notifyWhenRegistered(info)
    }

    override fun onChildBuilt(child: Node<*>) {
        notifyWhenChanged(Mode.ON_BUILT, child, node.children)
    }

    override fun onChildAttached(child: Node<*>) {
        notifyWhenChanged(Mode.ON_ATTACH, child, node.children)
    }

    private fun notifyWhenRegistered(callback: ChildAwareCallbackInfo) {
        when (callback) {
            is ChildAwareCallbackInfo.Double<*, *> -> callback.invokeIfRequired(node.children)
            is ChildAwareCallbackInfo.Single<*> -> callback.invokeIfRequired(node.children)
        }
    }

    private fun notifyWhenChanged(mode: Mode, child: Node<*>, nodes: List<Node<*>>) {
        for (callback in callbacks.filter { it.mode == mode }) {
            when (callback) {
                is ChildAwareCallbackInfo.Double<*, *> -> callback.invokeIfRequired(nodes, child)
                is ChildAwareCallbackInfo.Single<*> -> callback.invokeIfRequired(child)
            }
        }
    }

}
