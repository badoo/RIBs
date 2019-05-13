package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.NodeConnector
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConnectorCommand.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

internal class BackStackRibConnector<C : Parcelable> private constructor(
    private val binder: Binder,
    private val backStackManager: BackStackManager<C>,
    private val permanentParts: List<Node<*>>,
    private val resolver: (C) -> RoutingAction<*>,
    private val connector: NodeConnector
) : Disposable by binder {

    enum class DetachStrategy {
        DESTROY, DETACH_VIEW
    }

    constructor(
        backStackManager: BackStackManager<C>,
        permanentParts: List<Node<*>>,
        resolver: (C) -> RoutingAction<*>,
        connector: NodeConnector
    ) : this(
        binder = Binder(),
        backStackManager = backStackManager,
        permanentParts = permanentParts,
        resolver = resolver,
        connector = connector
    )

    // FIXME persist this to Bundle?
//    private val nodes: MutableList<BackStackElement<C>> = mutableListOf()

    private val backStackStateChangeToCommands = ConnectorCommandCreator<C>()
    private val executor = ConnectorCommandExecutor(
        resolver,
        connector
    )

    private val onConnectorCommand: Consumer<List<ConnectorCommand<C>>> = Consumer { commands ->
        commands.forEach { command ->
            val index = command.index
            when (command) {
                is Add -> executor.addConfiguration(index, command.configuration)
                is MakeActive -> executor.makeConfigurationActive(index)
                is MakePassive -> executor.makeConfigurationPassive(index)
                is Remove -> executor.removeConfiguration(index)
            }
        }
    }

    init {
        permanentParts.forEach {
            connector.attachChildNode(it)
        }

        val stateChange = Observable.wrap(backStackManager)
            .startWith(BackStackManager.State())
            .buffer(2, 1)
            .map { backStackStateChangeToCommands.invoke(it[0], it[1]) }

        binder.bind(stateChange to onConnectorCommand)
    }

//    // FIXME this can be done with local [nodes] field
//    fun shrinkToBundles(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> =
//        saveInstanceState(backStack).apply {
//            dropLast(1).forEach {
//                it.builtNodes?.forEach {
//                    connector.detachChildView(it.node)
//                    connector.detachChildNode(it.node)
//                }
//                it.builtNodes = null
//            }
//        }
//
//    // FIXME this can be done with local [nodes] field
//    fun saveInstanceState(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> {
//        backStack.forEach {
//            it.bundles = it.builtNodes?.map { nodeDescriptor ->
//                Bundle().also {
//                    nodeDescriptor.node.onSaveInstanceState(it)
//                }
//            } ?: emptyList()
//        }
//
//        return backStack
//    }

    fun detachFromView() {
        permanentParts.forEach { connector.detachChildView(it) }

        // FIXME +contents below overlays in last position
        executor.makeConfigurationPassive(backStackManager.state.backStack.lastIndex)
    }

    fun attachToView() {
        permanentParts.forEach { connector.attachChildView(it) }

        // FIXME +contents below overlays in last position
        executor.makeConfigurationActive(backStackManager.state.backStack.lastIndex)
    }
}
