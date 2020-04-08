package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.MultiConfigurationCommand.SaveInstanceState
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.MultiConfigurationCommand.Sleep
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.MultiConfigurationCommand.WakeUp
import com.badoo.ribs.core.routing.configuration.ConfigurationResolver
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature
import com.badoo.ribs.core.routing.configuration.feature.operation.BackStackOperation
import com.badoo.ribs.core.routing.configuration.toCommands
import com.badoo.ribs.core.view.RibView

abstract class Router<C : Parcelable, Permanent : C, Content : C, Overlay : C, V : RibView>(
    savedInstanceState: Bundle?,
    private val initialConfiguration: Content,
    private val permanentParts: List<Permanent> = emptyList()
) : ConfigurationResolver<C, V> {
    companion object {
        internal const val BUNDLE_KEY = "Router"
    }

    private val binder = Binder()
    private val savedInstanceState = savedInstanceState?.getBundle(BUNDLE_KEY)
    private val timeCapsule: AndroidTimeCapsule = AndroidTimeCapsule(this.savedInstanceState)
    private lateinit var backStackFeature: BackStackFeature<C>
    private lateinit var configurationFeature: ConfigurationFeature<C>
    lateinit var node: Node<V>
        private set

    internal fun init(node: Node<V>) {
        this.node = node
        initFeatures(node)
    }

    private fun initFeatures(node: Node<V>) {
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
    }

    fun onSaveInstanceState(outState: Bundle) {
        configurationFeature.accept(SaveInstanceState())
        val bundle = Bundle()
        timeCapsule.saveState(bundle)
        outState.putBundle(BUNDLE_KEY, bundle)
    }

    fun onLowMemory() {
        // TODO add back support for this
    }

    fun onAttach() {
        binder.bind(backStackFeature.toCommands() to configurationFeature)
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

    fun acceptOperation(backStackOperation: BackStackOperation<C>) {
        backStackFeature.accept(BackStackFeature.Operation.ExtendedOperation(backStackOperation))
    }

    internal fun acceptBackStack(acceptWish: BackStackFeature<C>.() -> Unit) {
        backStackFeature.acceptWish()
    }
}
