package com.badoo.ribs.clienthelper.childaware

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.lifecycle.MinimumCombinedLifecycle
import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.reflect.safeCast

internal sealed class ChildAwareCallbackInfo {
    abstract val mode: Mode

    class Single<T : Rib>(
        private val child: KClass<T>,
        private val callback: ChildCallback<T>,
        private val parentLifecycle: Lifecycle,
        override val mode: Mode,
    ) : ChildAwareCallbackInfo() {

        fun invokeIfRequired(newNode: Node<*>) {
            if (parentLifecycle.isDestroyed) return
            val castedNode = child.safeCast(newNode)
            if (castedNode != null) {
                val lifecycle =
                    MinimumCombinedLifecycle(
                        parentLifecycle,
                        newNode.lifecycle,
                    ).lifecycle
                @Suppress("UNCHECKED_CAST")
                callback(lifecycle, castedNode)
            }
        }

        fun invokeIfRequired(activeNodes: List<Node<*>>) {
            activeNodes.forEach { node ->
                invokeIfRequired(node)
            }
        }

    }

    @Suppress("LongParameterList")
    class Double<T1 : Rib, T2 : Rib>(
        private val child1: KClass<T1>,
        private val child2: KClass<T2>,
        private val callback: ChildrenCallback<T1, T2>,
        private val parentLifecycle: Lifecycle,
        override val mode: Mode,
    ) : ChildAwareCallbackInfo() {

        fun invokeIfRequired(activeNodes: List<Node<*>>, newNode: Node<*>) {
            val second = getOther(newNode) ?: return
            activeNodes
                .filter { second.isInstance(it) }
                .forEach {
                    notify(newNode, it)
                }
        }

        fun invokeIfRequired(activeNodes: List<Node<*>>) {
            activeNodes.forEachIndexed { index, node ->
                // Do not include already handled nodes to avoid call duplication
                invokeIfRequired(activeNodes.subList(index + 1, activeNodes.size), node)
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun notify(node1: Node<*>, node2: Node<*>) {
            if (parentLifecycle.isDestroyed) return
            val lifecycle =
                MinimumCombinedLifecycle(
                    parentLifecycle,
                    node1.lifecycle,
                    node2.lifecycle,
                ).lifecycle
            if (child1.isInstance(node1)) {
                callback(lifecycle, child1.cast(node1), child2.cast(node2))
            } else {
                callback(lifecycle, child1.cast(node2), child2.cast(node1))
            }
        }

        private fun getOther(node: Node<*>): KClass<*>? =
            when {
                child1.isInstance(node) -> child2
                child2.isInstance(node) -> child1
                else -> null
            }

    }

    enum class Mode {
        ON_BUILT, ON_ATTACH
    }

}
