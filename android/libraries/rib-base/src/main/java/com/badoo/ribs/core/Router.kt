package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.BackStackManager
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.NewRoot
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.Pop
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.Push
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.PushOverlay
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.Replace
import com.badoo.ribs.core.routing.backstack.BackStackRibConnector
import com.badoo.ribs.core.view.RibView

abstract class Router<C : Parcelable, V : RibView>(
    private val initialConfiguration: C
) {
    private lateinit var timeCapsule: AndroidTimeCapsule
    private lateinit var backStackManager: BackStackManager<C>
    private lateinit var backStackRibConnector: BackStackRibConnector<C>
    protected val configuration: C?
        get() = backStackManager.state.current

    lateinit var node: Node<V>
        internal set

    fun onAttach(savedInstanceState: Bundle?) {
        timeCapsule = AndroidTimeCapsule(savedInstanceState)
        initConfigurationManager()
    }

    protected open val permanentParts: List<() -> Node<*>> =
        emptyList()

    private fun initConfigurationManager() {
        backStackManager = BackStackManager(
            initialConfiguration = initialConfiguration,
            timeCapsule = timeCapsule
        )

        backStackRibConnector = BackStackRibConnector(
            backStackManager,
            permanentParts.map { it.invoke() },
            this::resolveConfiguration,
            node
        )
    }

    abstract fun resolveConfiguration(configuration: C): RoutingAction<V>

    fun onSaveInstanceState(outState: Bundle) {
        // FIXME
//        backStackManager.accept(SaveInstanceState())
        timeCapsule.saveState(outState)
    }

    fun onLowMemory() {
        // FIXME
//        backStackManager.accept(ShrinkToBundles())
    }

    fun onAttachView() {
        backStackRibConnector.attachToView()
    }

    fun onDetachView() {
        backStackRibConnector.detachFromView()
    }

    fun onDetach() {
        backStackRibConnector.dispose()
    }

    fun replace(configuration: C) {
        backStackManager.accept(Replace(configuration))
    }

    fun push(configuration: C) {
        if (configuration is Overlay) {
            backStackManager.accept(PushOverlay(configuration))
        } else {
            backStackManager.accept(Push(configuration))
        }
    }

    fun newRoot(configuration: C) {
        backStackManager.accept(NewRoot(configuration))
    }

    fun popBackStack(): Boolean {
        return if (backStackManager.state.canPop) {
            backStackManager.accept(Pop())
            true
        } else {
            false
        }
    }

    /**
     * Marker interface for Configurations that should be added as overlays
     * i.e. not detaching previous Configurations
     */
    interface Overlay
}
