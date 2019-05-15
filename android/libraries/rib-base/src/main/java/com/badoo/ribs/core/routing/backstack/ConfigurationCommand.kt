package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Deactivate
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved

internal sealed class ConfigurationCommand<C : Parcelable> {

    sealed class Global<C : Parcelable> : ConfigurationCommand<C>() {

        abstract fun execute(pool: Map<ConfigurationKey, ConfigurationContext<C>>, parentNode: Node<*>)

        class Sleep<C : Parcelable> : Global<C>() {
            // TODO passing in the key to construct Activate is conceptually wrong here
            private val deactivate = Deactivate<C>(ConfigurationKey.Permanent(-1))

            override fun execute(pool: Map<ConfigurationKey, ConfigurationContext<C>>, parentNode: Node<*>) {
                pool.invokeOn(ACTIVE) { deactivate.execute(it, parentNode) }
            }
        }
        class WakeUp<C : Parcelable> : Global<C>() {
            // TODO passing in the key to construct Activate is conceptually wrong here
            private val activate = Activate<C>(ConfigurationKey.Permanent(-1))

            override fun execute(pool: Map<ConfigurationKey, ConfigurationContext<C>>, parentNode: Node<*>) {
                pool.invokeOn(SLEEPING) { activate.execute(it, parentNode) }
            }
        }

        protected fun Map<ConfigurationKey, ConfigurationContext<C>>.invokeOn(
            activationState: ConfigurationContext.ActivationState,
            block: (Resolved<C>) -> Unit
        ) {
            this
                .filter { it.value is Resolved<*> && it.value.activationState == activationState }
                .map { it.key to it.value as Resolved<C> }
                .forEach { (_, resolved) ->
                    block.invoke(resolved)
                }
        }
    }

    sealed class Individual<C : Parcelable> : ConfigurationCommand<C>() {
        abstract val key: ConfigurationKey

        abstract fun execute(item: Resolved<C>, parentNode: Node<*>)

        data class Add<C : Parcelable>(override val key: ConfigurationKey, val configuration: C) : Individual<C>() {
            override fun execute(item: Resolved<C>, parentNode: Node<*>) {
                // no-op TODO reconsider
            }
        }

        data class Activate<C : Parcelable>(override val key: ConfigurationKey) : Individual<C>() {
            override fun execute(item: Resolved<C>, parentNode: Node<*>) {
                parentNode.attachParentedViews(item.nodes)
                item.routingAction.execute()
            }

            private fun Node<*>.attachParentedViews(nodes: List<Node.Descriptor>) {
                nodes.forEach {
                    if (it.viewAttachMode == Node.ViewAttachMode.PARENT && !it.node.isViewAttached) {
                        attachChildView(it.node)
                    }
                }
            }
        }

        data class Deactivate<C : Parcelable>(override val key: ConfigurationKey) : Individual<C>() {
            override fun execute(item: Resolved<C>, parentNode: Node<*>) {
                item.routingAction.cleanup()
                item.nodes.saveViewState()
                parentNode.detachChildViews(item.nodes)
            }

            private fun List<Node.Descriptor>.saveViewState() {
                forEach {
                    it.node.saveViewState()
                }
            }

            private fun Node<*>.detachChildViews(nodes: List<Node.Descriptor>) {
                nodes.forEach {
                    it.node.saveViewState()

                    if (it.viewAttachMode == Node.ViewAttachMode.PARENT) {
                        detachChildView(it.node)
                    }
                }
            }
        }

        data class Remove<C : Parcelable>(override val key: ConfigurationKey) : Individual<C>() {
            override fun execute(item: Resolved<C>, parentNode: Node<*>) {
                item.nodes.forEach {
                    parentNode.detachChildView(it.node)
                    parentNode.detachChildNode(it.node)
                }
            }
        }
    }
}
