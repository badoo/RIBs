package com.badoo.ribs.core.routing.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Node.ViewAttachMode.PARENT
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Global.Sleep
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Global.WakeUp
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Add
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Deactivate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Remove
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Unresolved
import io.reactivex.functions.Consumer

internal class ConfigurationHandler<C : Parcelable>(
    private val resolver: (C) -> RoutingAction<*>,
    private val parentNode: Node<*>
) : Consumer<List<ConfigurationCommand<out C>>> {

    // FIXME persist this to Bundle?
    internal val pool: MutableMap<Int, ConfigurationContext<C>> = mutableMapOf()
    private var activationLevel = SLEEPING

    override fun accept(commands: List<ConfigurationCommand<out C>>) {
        commands.forEach {
            when (it) {
                is ConfigurationCommand.Individual -> it.handle()
                is ConfigurationCommand.Global -> when (it) {
                    is Sleep -> sleep()
                    is WakeUp -> wakeUp()
                }
            }
        }
    }

    private fun ConfigurationCommand.Individual<out C>.handle() {
        val index = this.index
        if (this is Add) {
            // TODO check for accidental override?
            pool[index] = Unresolved(this.configuration)
        }

        val resolved = resolve(index)

        when (this) {
            is Add -> { /* nop, resolve already handled */ }
            is Activate -> resolved.activate(index)
            is Deactivate -> resolved.deactivate(index)
            is Remove -> resolved.remove(index)
        }
    }

    private fun resolve(index: Int): Resolved<C> {
        if (!pool.containsKey(index)) {
            throw RuntimeException("Key $index was not found in pool")
        }

        return when (val item = pool[index]!!) {
            is Resolved -> item
            is Unresolved -> {
                val routingAction = resolver.invoke(item.configuration)
                val resolved = Resolved(
                    configuration = item.configuration,
                    routingAction = routingAction,
                    builtNodes = routingAction.buildNodes(),
                    bundles = emptyList(),
                    activationState = INACTIVE
                ).also {
                    it.add()
                }

                pool[index] = resolved
                resolved
            }
        }
    }

    private fun Resolved<C>.add() {
        builtNodes.forEachIndexed { index, nodeDescriptor ->
            parentNode.attachChildNode(nodeDescriptor.node, bundleAt(index))
        }
    }

    private fun Resolved<C>.bundleAt(index: Int): Bundle? =
        bundles.elementAtOrNull(index)?.also {
            it.classLoader = ConfigurationContext::class.java.classLoader
        }

    private fun Resolved<C>.activate(index: Int) {
        reAttachViewsIfNeeded()
        routingAction.execute()
        pool[index] = this.copy(
            activationState = activationLevel
        )
    }

    private fun Resolved<C>.reAttachViewsIfNeeded() {
        builtNodes
            .forEach {
                if (it.viewAttachMode == PARENT && !it.node.isViewAttached) {
                    parentNode.attachChildView(it.node)
                }
            }
    }

    private fun Resolved<C>.deactivate(index: Int) {
        routingAction.cleanup()
        saveAndDetachChildViews()
        pool[index] = this.copy(
            activationState = INACTIVE
        )
    }

    private fun Resolved<C>.saveAndDetachChildViews() {
        builtNodes.forEach {
            it.node.saveViewState()

            if (it.viewAttachMode == PARENT) {
                parentNode.detachChildView(it.node)
            }
        }
    }

    private fun Resolved<C>.remove(index: Int) {
        pool[index].run {
            destroyChildren()
        }
        pool.remove(index)
    }

    private fun Resolved<C>.destroyChildren() {
        builtNodes.forEach {
            parentNode.detachChildView(it.node)
            parentNode.detachChildNode(it.node)
        }
    }

    private fun sleep() {
        activationLevel = SLEEPING
        transition(ACTIVE) { key, entry -> entry.deactivate(key) }
    }

    private fun wakeUp() {
        activationLevel = ACTIVE
        transition(SLEEPING) { key, entry ->
            entry.activate(key)
        }
    }

    private fun transition(
        onElementsWith: ActivationState,
        onTransition: (Int, Resolved<C>) -> Unit
    ) {
        pool
            .filter { it.value is Resolved<*> && it.value.activationState == onElementsWith }
            .map { it.key to it.value as Resolved<C> }
            .forEach {
                val (key, entry) = it
                onTransition.invoke(key, entry)
                pool[key] = entry.copy(
                    activationState = activationLevel
                )
            }
    }

//    // FIXME this can be done with local [nodes] field
//    fun shrinkToBundles(pool: List<BackStackElement>): List<BackStackElement> =
//        saveInstanceState(pool).apply {
//            dropLast(1).forEach {
//                it.builtNodes?.forEach {
//                    parentNode.detachChildView(it.node)
//                    parentNode.detachChildNode(it.node)
//                }
//                it.builtNodes = null
//            }
//        }
//
//    // FIXME this can be done with local [nodes] field
//    fun saveInstanceState(pool: List<BackStackElement>): List<BackStackElement> {
//        pool.forEach {
//            it.bundles = it.builtNodes?.map { nodeDescriptor ->
//                Bundle().also {
//                    nodeDescriptor.node.onSaveInstanceState(it)
//                }
//            } ?: emptyList()
//        }
//
//        return pool
//    }

//    fun detachFromView() {
//        permanentParts.forEach { parentNode.detachChildView(it) }
//
//        // FIXME +contents below overlays in last position
//        deactivate(backStackManager.state.pool.lastIndex)
//    }
//
//    fun attachToView() {
//        permanentParts.forEach { parentNode.attachChildView(it) }
//
//        // FIXME +contents below overlays in last position
//        activate(backStackManager.state.pool.lastIndex)
//    }
}
