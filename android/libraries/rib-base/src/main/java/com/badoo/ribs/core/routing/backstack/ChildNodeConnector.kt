package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Global
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.Add
import com.badoo.ribs.core.routing.backstack.ConfigurationKey.Permanent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

// TODO merge into ConfigurationFeature and delete
internal class ChildNodeConnector<C : Parcelable> private constructor(
    private val binder: Binder,
    private val backStackFeature: BackStackFeature<C>,
    private val permanentParts: List<C>,
    private val resolver: (C) -> RoutingAction<*>,
    private val parentNode: Node<*>
) : Disposable by binder {

    constructor(
        backStackFeature: BackStackFeature<C>,
        permanentParts: List<C>,
        resolver: (C) -> RoutingAction<*>,
        parentNode: Node<*>
    ) : this(
        binder = Binder(),
        backStackFeature = backStackFeature,
        permanentParts = permanentParts,
        resolver = resolver,
        parentNode = parentNode
    )

    private val backStackStateChangeToCommands = ConfigurationCommandCreator<C>()
    private val configurationHandler = ConfigurationFeature(
        // FIXME timecapsule
        AndroidTimeCapsule(null),
        resolver,
        parentNode
    )

    init {
        permanentParts.forEachIndexed { index, configuration ->
            val key = Permanent(index)
            configurationHandler.accept(Add(key, configuration))
            configurationHandler.accept(Activate(key))
        }

        val commands = Observable.wrap(backStackFeature)
            .startWith(BackStackFeature.State())
            .buffer(2, 1)
            .map { backStackStateChangeToCommands.invoke(it[0], it[1]) }
            .flatMapIterable { items -> items }

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
//        configurationHandler.pool.forEach {
//            val key = it.key
//            val entry = it.value
//
//            if (entry is ConfigurationContext.Resolved<C>) {
//                configurationHandler.pool[key] = entry.copy(
//                    bundles = entry.builtNodes.map { nodeDescriptor ->
//                        Bundle().also {
//                            nodeDescriptor.node.onSaveInstanceState(it)
//                        }
//                    }
//                )
//            }
//        }
    }

    fun detachFromView() {
        configurationHandler.accept(
            Global.Sleep()
        )
    }

    fun attachToView() {
        configurationHandler.accept(
            Global.WakeUp()
        )
    }
}
