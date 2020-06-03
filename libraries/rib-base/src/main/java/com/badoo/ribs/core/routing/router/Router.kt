package com.badoo.ribs.core.routing.router

import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.plugin.ViewLifecycleAware
import com.badoo.ribs.core.routing.activator.ChildActivator
import com.badoo.ribs.core.routing.activator.RoutingActivator
import com.badoo.ribs.core.routing.activator.UnhandledChildActivator
import com.badoo.ribs.core.routing.resolver.RoutingResolver
import com.badoo.ribs.core.routing.state.feature.Transaction.PoolCommand.SaveInstanceState
import com.badoo.ribs.core.routing.state.feature.Transaction.PoolCommand.Sleep
import com.badoo.ribs.core.routing.state.feature.Transaction.PoolCommand.WakeUp
import com.badoo.ribs.core.routing.state.feature.ConfigurationFeature
import com.badoo.ribs.core.routing.source.changes
import com.badoo.ribs.core.routing.source.RoutingSource
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
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

    private lateinit var configurationFeature: ConfigurationFeature<C>
    override lateinit var node: Node<*>
    private lateinit var routingExecutor: RoutingActivator<C>

    override fun init(node: Node<*>) {
        this.node = node
        routingExecutor = RoutingActivator(node, clientChildActivator)
        initFeatures(node)
    }

    private fun initFeatures(node: Node<*>) {
        configurationFeature = ConfigurationFeature(
            timeCapsule = timeCapsule,
            resolver = this,
            activator = routingExecutor,
            parentNode = node, // TODO remove
            transitionHandler = transitionHandler
        )

        disposables.add(configurationFeature)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        routingSource.onSaveInstanceState(outState)
        configurationFeature.accept(SaveInstanceState())
        timeCapsule.saveState(outState)
    }

    override fun onAttach(nodeLifecycle: Lifecycle) {
        binder.bind(routingSource.changes(hasSavedState) to configurationFeature)
    }

    override fun onAttachToView(parentViewGroup: ViewGroup) {
        configurationFeature.accept(WakeUp())
    }

    override fun onDetachFromView(parentViewGroup: ViewGroup) {
        configurationFeature.accept(Sleep())
    }

    override fun onDetach() {
        // TODO consider extending Disposables plugin
        binder.dispose()
        configurationFeature.dispose()
    }
}
