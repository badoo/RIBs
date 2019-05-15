package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Add
import com.badoo.ribs.core.routing.backstack.ConfigurationKey.Permanent
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature
import com.badoo.ribs.core.routing.backstack.feature.ConfigurationFeature
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

    private val configurationFeature =
        ConfigurationFeature(
            // FIXME timecapsule
            AndroidTimeCapsule(null),
            resolver,
            parentNode
        )

    init {
        permanentParts.forEachIndexed { index, configuration ->
            val key = Permanent(index)
            configurationFeature.accept(Add(key, configuration))
            configurationFeature.accept(Activate(key))
        }

        binder.bind(backStackFeature.commands() to configurationFeature)
    }

////    // FIXME this can be done with local [nodes] field
//    fun shrinkToBundles() {
//        saveInstanceState().apply {
//            dropLast(1).forEach {
//                it.nodes?.forEach {
//                    parentNode.detachChildView(it.node)
//                    parentNode.detachChildNode(it.node)
//                }
//                it.nodes = null
//            }
//        }
//    }

    fun saveInstanceState() {
//        configurationFeature.pool.forEach {
//            val key = it.key
//            val entry = it.value
//
//            if (entry is ConfigurationContext.Resolved<C>) {
//                configurationFeature.pool[key] = entry.copy(
//                    bundles = entry.nodes.map { nodeDescriptor ->
//                        Bundle().also {
//                            nodeDescriptor.node.onSaveInstanceState(it)
//                        }
//                    }
//                )
//            }
//        }
    }

    fun detachFromView() {
        configurationFeature.accept(
            MultiConfigurationCommand.Sleep()
        )
    }

    fun attachToView() {
        configurationFeature.accept(
            MultiConfigurationCommand.WakeUp()
        )
    }
}
