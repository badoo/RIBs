package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.ViewGroup
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.core.routing.NodeConnector
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.BackStackManager
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.NewRoot
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.Pop
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.Push
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.Replace
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.SaveInstanceState
import com.badoo.ribs.core.routing.backstack.BackStackManager.Wish.ShrinkToBundles
import com.badoo.ribs.core.routing.backstack.BackStackRibConnector
import com.badoo.ribs.core.view.RibView

abstract class Router<C : Parcelable, V : RibView>(
    private val initialConfiguration: C
) {
    private val binder = Binder()
    private lateinit var timeCapsule: AndroidTimeCapsule
    private lateinit var backStackManager: BackStackManager<C>
    private lateinit var backStackRibConnector: BackStackRibConnector<C>
    protected val configuration: C?
        get() = backStackManager.state.current.configuration

    lateinit var node: Node<V>
        internal set

    fun onAttach(savedInstanceState: Bundle?) {
        timeCapsule = AndroidTimeCapsule(savedInstanceState)
        initConfigurationManager()
    }

    protected open val permanentParts: List<() -> Node<*>> =
        emptyList()

    private fun initConfigurationManager() {
        backStackRibConnector = BackStackRibConnector(
            permanentParts.map { it.invoke() },
            this::resolveConfiguration,
            NodeConnector.from(
                node::attachChildNode,
                node::attachChildView,
                node::detachChildView,
                node::detachChildNode
            )
        )

        backStackManager = BackStackManager(
            backStackRibConnector = backStackRibConnector,
            initialConfiguration = initialConfiguration,
            timeCapsule = timeCapsule
        )
    }

    abstract fun resolveConfiguration(configuration: C): RoutingAction<V>

    /**
     * @param view is null only in the case when the supplied ViewFactory in the Node is null,
     *              and as such, it should be referenced by view!! when overriding this method
     *              to catch any problems instead of silently returning null
     */
    open fun getParentViewForChild(child: Rib, view: V?): ViewGroup? =
        view?.androidView

    fun onSaveInstanceState(outState: Bundle) {
        backStackManager.accept(SaveInstanceState())
        timeCapsule.saveState(outState)
    }

    fun onLowMemory() {
        backStackManager.accept(ShrinkToBundles())
    }

    fun onAttachView() {
        backStackRibConnector.attachToView(backStackManager.state.backStack)
    }

    fun onDetachView() {
        backStackRibConnector.detachFromView(backStackManager.state.backStack)
    }

    fun onDetach() {
        binder.clear()
    }

    fun replace(configuration: C) {
        backStackManager.accept(Replace(configuration))
    }

    fun push(configuration: C) {
        backStackManager.accept(Push(configuration))
    }

    fun newRoot(configuration: C) {
        backStackManager.accept(NewRoot(configuration))
    }

    fun popBackStack(): Boolean {
        return if (backStackManager.state.canPop) {
            Log.d("Back stack before pop", backStackManager.state.toString())
            backStackManager.accept(Pop())
            Log.d("Back stack after pop", backStackManager.state.toString())
            true
        } else {
            false
        }
    }
}
