package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand.SaveInstanceState
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand.Sleep
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand.WakeUp
import com.badoo.ribs.core.routing.backstack.commands
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation.NewRoot
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation.Pop
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation.Push
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation.PushOverlay
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation.Replace
import com.badoo.ribs.core.routing.backstack.feature.ConfigurationFeature
import com.badoo.ribs.core.view.RibView

abstract class Router<C : Parcelable, Permanent : C, Content : C, Overlay : C, V : RibView>(
    private val initialConfiguration: Content,
    private val permanentParts: List<Permanent> = emptyList()
) {
    private val binder = Binder()
    private lateinit var timeCapsule: AndroidTimeCapsule
    private lateinit var backStackFeature: BackStackFeature<C>
    private lateinit var configurationFeature: ConfigurationFeature<C>
    lateinit var node: Node<V>
        internal set

    fun onAttach(savedInstanceState: Bundle?) {
        timeCapsule = AndroidTimeCapsule(savedInstanceState)
        initFeatures()
    }

    private fun initFeatures() {
        backStackFeature = BackStackFeature(
            initialConfiguration = initialConfiguration,
            timeCapsule = timeCapsule
        )

        configurationFeature = ConfigurationFeature(
            initialConfigurations = permanentParts,
            timeCapsule = timeCapsule,
            resolver = this::resolveConfiguration,
            parentNode = node
        )

        binder.bind(backStackFeature.commands() to configurationFeature)
    }

    abstract fun resolveConfiguration(configuration: C): RoutingAction<V>

    fun onSaveInstanceState(outState: Bundle) {
        configurationFeature.accept(SaveInstanceState())
        timeCapsule.saveState(outState)
    }

    fun onLowMemory() {
        // TODO add back support for this
    }

    fun onAttachView() {
        configurationFeature.accept(WakeUp())
    }

    fun onDetachView() {
        configurationFeature.accept(Sleep())
    }

    fun onDetach() {
        binder.dispose()
    }

    fun replace(configuration: Content) {
        backStackFeature.accept(Replace(configuration))
    }

    fun push(configuration: Content) {
        backStackFeature.accept(Push(configuration))
    }

    fun pushOverlay(configuration: Overlay) {
        backStackFeature.accept(PushOverlay(configuration))
    }

    fun newRoot(configuration: Content) {
        backStackFeature.accept(NewRoot(configuration))
    }

    fun popBackStack(): Boolean =
        if (backStackFeature.state.canPop) {
            backStackFeature.accept(Pop())
            true
        } else {
            false
        }
}
