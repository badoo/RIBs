package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.NewRoot
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.Pop
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.Push
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.PushOverlay
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.Replace
import com.badoo.ribs.core.routing.backstack.ChildNodeConnector
import com.badoo.ribs.core.view.RibView

abstract class Router<C : Parcelable, Permanent : C, Content : C, Overlay : C, V : RibView>(
    private val initialConfiguration: C
) {
    private lateinit var timeCapsule: AndroidTimeCapsule
    private lateinit var backStackFeature: BackStackFeature<C>
    private lateinit var childNodeConnector: ChildNodeConnector<C>
    protected val configuration: C?
        get() = backStackFeature.state.current

    lateinit var node: Node<V>
        internal set

    fun onAttach(savedInstanceState: Bundle?) {
        timeCapsule = AndroidTimeCapsule(savedInstanceState)
        initConfigurationManager()
    }

    protected open val permanentParts: List<Permanent> =
        emptyList()

    private fun initConfigurationManager() {
        backStackFeature = BackStackFeature(
            initialConfiguration = initialConfiguration,
            timeCapsule = timeCapsule
        )

        childNodeConnector = ChildNodeConnector(
            backStackFeature,
            permanentParts,
            this::resolveConfiguration,
            node
        )
    }

    abstract fun resolveConfiguration(configuration: C): RoutingAction<V>

    fun onSaveInstanceState(outState: Bundle) {
        // FIXME
//        backStackFeature.accept(SaveInstanceState())
        timeCapsule.saveState(outState)
    }

    fun onLowMemory() {
        // FIXME
//        backStackFeature.accept(ShrinkToBundles())
    }

    fun onAttachView() {
        childNodeConnector.attachToView()
    }

    fun onDetachView() {
        childNodeConnector.detachFromView()
    }

    fun onDetach() {
        childNodeConnector.dispose()
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

    fun popBackStack(): Boolean {
        return if (backStackFeature.state.canPop) {
            backStackFeature.accept(Pop())
            true
        } else {
            false
        }
    }
}
