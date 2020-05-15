package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.BackPressHandler
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.ViewLifecycleAware
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Activate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.ConfigurationResolver
import com.badoo.ribs.core.routing.configuration.Transaction
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.SaveInstanceState
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.Sleep
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.WakeUp
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature
import com.badoo.ribs.core.routing.configuration.feature.operation.BackStackOperation
import com.badoo.ribs.core.routing.configuration.feature.operation.Pop
import com.badoo.ribs.core.routing.configuration.feature.operation.canPop
import com.badoo.ribs.core.routing.configuration.feature.operation.canPopOverlay
import com.badoo.ribs.core.routing.configuration.toCommands
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import com.badoo.ribs.core.view.RibView

abstract class Router<C : Parcelable, Permanent : C, Content : C, Overlay : C, V : RibView>(
    buildParams: BuildParams<*>,
    private val initialConfiguration: Content,
    private val permanentParts: List<Permanent> = emptyList(),
    private val transitionHandler: TransitionHandler<C>? = null
) : NodeAware,
    NodeLifecycleAware,
    ViewLifecycleAware,
    SavesInstanceState,
    BackPressHandler,
    ConfigurationResolver<C> {

    companion object {
        internal const val BUNDLE_KEY = "Router"
    }

    private val binder = Binder()
    private val savedInstanceState = buildParams.savedInstanceState?.getBundle(BUNDLE_KEY)
    private val timeCapsule: AndroidTimeCapsule = AndroidTimeCapsule(this.savedInstanceState)
    private lateinit var backStackFeature: BackStackFeature<C>
    private lateinit var configurationFeature: ConfigurationFeature<C>
    lateinit var node: Node<*>
        private set

    override fun init(node: Node<*>) {
        this.node = node
        initFeatures(node)
    }

    private fun initFeatures(node: Node<*>) {
        backStackFeature = BackStackFeature(
            initialConfiguration = initialConfiguration,
            timeCapsule = timeCapsule
        )

        configurationFeature = ConfigurationFeature(
            initialConfigurations = permanentParts,
            timeCapsule = timeCapsule,
            resolver = this::resolveConfiguration,
            parentNode = node,
            transitionHandler = transitionHandler
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        configurationFeature.accept(SaveInstanceState())
        val bundle = Bundle()
        timeCapsule.saveState(bundle)
        outState.putBundle(BUNDLE_KEY, bundle)
    }

    override fun onAttach(nodeLifecycle: Lifecycle) {
        binder.bind(backStackFeature.toCommands() to configurationFeature)
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
        backStackFeature.dispose()
        configurationFeature.dispose()
    }

    fun acceptOperation(backStackOperation: BackStackOperation<C>) {
        backStackFeature.accept(BackStackFeature.Operation(backStackOperation))
    }

    internal fun add(configurationKey: ConfigurationKey<C>) {
        configurationFeature.accept(
            Transaction.from(
                Add(configurationKey)
            )
        )
    }

    internal fun remove(configurationKey: ConfigurationKey<C>) {
        configurationFeature.accept(
            Transaction.from(
                Remove(configurationKey)
            )
        )
    }

    internal fun activate(configurationKey: ConfigurationKey<C>) {
        configurationFeature.accept(
            Transaction.from(
                Activate(configurationKey)
            )
        )
    }

    internal fun deactivate(configurationKey: ConfigurationKey<C>) {
        configurationFeature.accept(
            Transaction.from(
                Deactivate(configurationKey)
            )
        )
    }

    internal fun getNodes(configurationKey: ConfigurationKey<C>): List<Node<*>>? =
        (configurationFeature.state.pool[configurationKey] as? ConfigurationContext.Resolved<C>)?.nodes

    fun popBackStack(): Boolean =
        if (backStackFeature.state.backStack.canPop) {
            acceptOperation(Pop())
            true
        } else {
            false
        }

    fun popOverlay(): Boolean =
        if (backStackFeature.state.backStack.canPopOverlay) {
            acceptOperation(Pop())
            true
        } else {
            false
        }
}
