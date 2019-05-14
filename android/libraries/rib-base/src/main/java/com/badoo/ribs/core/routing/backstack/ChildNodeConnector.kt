package com.badoo.ribs.core.routing.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

internal class ChildNodeConnector<C : Parcelable> private constructor(
    private val binder: Binder,
    private val backStackManager: BackStackManager<C>,
    private val permanentParts: List<Node<*>>,
    private val resolver: (C) -> RoutingAction<*>,
    private val parentNode: Node<*>
) : Disposable by binder {

    constructor(
        backStackManager: BackStackManager<C>,
        permanentParts: List<Node<*>>,
        resolver: (C) -> RoutingAction<*>,
        parentNode: Node<*>
    ) : this(
        binder = Binder(),
        backStackManager = backStackManager,
        permanentParts = permanentParts,
        resolver = resolver,
        parentNode = parentNode
    )

    // FIXME persist this to Bundle?
//    private val nodes: MutableList<BackStackElement<C>> = mutableListOf()

    private val backStackStateChangeToCommands = ConfigurationCommandCreator<C>()
    private val configurationHandler = ConfigurationHandler(
        resolver,
        parentNode
    )

    init {
        permanentParts.forEach {
            parentNode.attachChildNode(it, null)
        }

        val commands = Observable.wrap(backStackManager)
            .startWith(BackStackManager.State())
            .buffer(2, 1)
            .map { backStackStateChangeToCommands.invoke(it[0], it[1]) }

        binder.bind(commands to configurationHandler)
    }

////    // FIXME this can be done with local [nodes] field
//    fun shrinkToBundles() {
//        saveInstanceState().apply {
//            dropLast(1).forEach {
//                it.builtNodes?.forEach {
//                    parentNode.detachChildView(it.node)
//                    parentNode.detachChildNode(it.node)
//                }
//                it.builtNodes = null
//            }
//        }
//    }

    fun saveInstanceState() {
        configurationHandler.pool.forEach {
            val key = it.key
            val entry = it.value

            if (entry is ConfigurationContext.Resolved<C>) {
                configurationHandler.pool[key] = entry.copy(
                    bundles = entry.builtNodes.map { nodeDescriptor ->
                        Bundle().also {
                            nodeDescriptor.node.onSaveInstanceState(it)
                        }
                    }
                )
            }
        }
    }

    fun detachFromView() {
        // TODO move permanent parts to Key-based item in configurationHandler
        permanentParts.forEach { parentNode.detachChildView(it) }

        configurationHandler.accept(
            listOf(
                ConfigurationCommand.Global.Sleep()
            )
        )
    }

    fun attachToView() {
        // TODO move permanent parts to Key-based item in configurationHandler
        permanentParts.forEach { parentNode.attachChildView(it) }

        configurationHandler.accept(
            listOf(
                ConfigurationCommand.Global.WakeUp()
            )
        )
    }
}
