package com.badoo.ribs.routing.router

import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.plugin.ViewLifecycleAware
import com.badoo.ribs.routing.activator.ChildActivator
import com.badoo.ribs.routing.activator.RoutingActivator
import com.badoo.ribs.routing.activator.UnhandledChildActivator
import com.badoo.ribs.routing.resolver.RoutingResolver
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.changes
import com.badoo.ribs.routing.state.feature.RoutingStatePool
import com.badoo.ribs.routing.state.feature.Transaction.PoolCommand.SaveInstanceState
import com.badoo.ribs.routing.state.feature.Transaction.PoolCommand.Sleep
import com.badoo.ribs.routing.state.feature.Transaction.PoolCommand.WakeUp
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import io.reactivex.disposables.CompositeDisposable

abstract class Router<C : Parcelable>(
    buildParams: BuildParams<*>,
    protected val routingSource: RoutingSource<C>,
    private val transitionHandler: TransitionHandler<C>? = null,
    private val clientChildActivator: ChildActivator<C> = UnhandledChildActivator()
) : RoutingResolver<C>,
    NodeAware,
    NodeLifecycleAware,
    ViewLifecycleAware,
    SavesInstanceState,
    SubtreeBackPressHandler by routingSource {

    private val binder = Binder()
    private val disposables = CompositeDisposable()
    private val timeCapsule: AndroidTimeCapsule = AndroidTimeCapsule(buildParams.savedInstanceState)
    private val hasSavedState: Boolean  = buildParams.savedInstanceState != null

    private lateinit var routingStatePool: RoutingStatePool<C>
    override lateinit var node: Node<*>
    private lateinit var activator: RoutingActivator<C>

    override fun init(node: Node<*>) {
        this.node = node
        activator = RoutingActivator(node, clientChildActivator)
        initFeatures(node)
    }

    private fun initFeatures(node: Node<*>) {
        routingStatePool = RoutingStatePool(
            timeCapsule = timeCapsule,
            resolver = this,
            activator = activator,
            parentNode = node, // TODO remove
            transitionHandler = transitionHandler
        )

        disposables.add(routingStatePool)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        routingSource.onSaveInstanceState(outState)
        routingStatePool.accept(SaveInstanceState())
        timeCapsule.saveState(outState)
    }

    override fun onAttach(nodeLifecycle: Lifecycle) {
        binder.bind(routingSource.changes(hasSavedState) to routingStatePool)
    }

    override fun onAttachToView(parentViewGroup: ViewGroup) {
        routingStatePool.accept(WakeUp())
    }

    override fun onDetachFromView(parentViewGroup: ViewGroup) {
        routingStatePool.accept(Sleep())
    }

    override fun onDetach() {
        // TODO consider extending Disposables plugin
        binder.dispose()
        routingStatePool.dispose()
    }
}
