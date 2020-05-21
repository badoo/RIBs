//package com.badoo.ribs.core.plugin
//
//import android.os.Bundle
//import android.os.Parcelable
//import android.view.ViewGroup
//import androidx.lifecycle.Lifecycle
//import com.badoo.mvicore.android.AndroidTimeCapsule
//import com.badoo.mvicore.binder.Binder
//import com.badoo.ribs.core.Node
//import com.badoo.ribs.core.builder.BuildParams
//import com.badoo.ribs.core.routing.RoutingSource
//import com.badoo.ribs.core.routing.configuration.ConfigurationContext
//import com.badoo.ribs.core.routing.configuration.ConfigurationKey
//import com.badoo.ribs.core.routing.configuration.ConfigurationResolver
//import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.SaveInstanceState
//import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.Sleep
//import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.WakeUp
//import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature
//import com.badoo.ribs.core.routing.configuration.toCommands
//import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
//import io.reactivex.disposables.CompositeDisposable
//
//abstract class Router<C : Parcelable, Permanent : C>(
//    buildParams: BuildParams<*>,
//    private val routingSource: RoutingSource<C>,
//    private val permanentParts: List<Permanent> = emptyList(),
//    private val transitionHandler: TransitionHandler<C>? = null
//) : ConfigurationResolver<C>,
//    NodeAware,
//    NodeLifecycleAware,
//    ViewLifecycleAware,
//    BackPressHandler,
//    SavesInstanceState {
//
//    private val binder = Binder()
//    private val disposables = CompositeDisposable()
//    private val timeCapsule: AndroidTimeCapsule = AndroidTimeCapsule(buildParams.savedInstanceState)
//
//
//    private lateinit var configurationFeature: ConfigurationFeature<C>
////    lateinit var node: Node<*>
////        private set
//
//    override fun init(node: Node<*>) {
////        this.node = node
//        initFeatures(node)
//    }
//
//    private fun initFeatures(node: Node<*>) {
//        configurationFeature = ConfigurationFeature(
//            initialConfigurations = permanentParts,
//            timeCapsule = timeCapsule,
//            resolver = this,
//            parentNode = node,
//            transitionHandler = transitionHandler
//        )
//
//        disposables.add(configurationFeature)
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        configurationFeature.accept(SaveInstanceState())
//        timeCapsule.saveState(outState)
//    }
//
//    override fun onAttach(ribLifecycle: Lifecycle) {
//        binder.bind(routingSource.toCommands() to configurationFeature)
//    }
//
//    override fun onAttachToView(parentViewGroup: ViewGroup) {
//        configurationFeature.accept(WakeUp())
//    }
//
//    override fun onDetachFromView(parentViewGroup: ViewGroup) {
//        configurationFeature.accept(Sleep())
//    }
//
//    override fun onDetach() {
//        // TODO consider extending Disposables plugin
//        binder.dispose()
//        configurationFeature.dispose()
//    }
//
//    // FIXME this shouldn't be here
//    internal fun getNodes(configurationKey: ConfigurationKey<C>): List<Node<*>>? =
//        (configurationFeature.state.pool[configurationKey] as? ConfigurationContext.Resolved<C>)?.nodes
//}
