/*
 * Copyright (C) 2017. Uber Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import android.util.SparseArray
import android.view.ViewGroup
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.uber.rib.util.RibRefWatcher
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Responsible for handling the addition and removal of child nodes.
 **/
open class Node<V : RibView>(
    internal open val identifier: Rib,
    private val viewFactory: ViewFactory<V>?,
    private val router: Router<*, *, *, *, V>,
    private val interactor: Interactor<*, *, *, V>

//    ,
//    private val ribRefWatcher: RibRefWatcher = RibRefWatcher.getInstance()
) {
    enum class ViewAttachMode {
        /**
         * The node's view attach/detach is managed by its parent.
         */
        PARENT,

        /**
         * The node's view is somewhere else in the view tree, and it should not be managed
         *  by its parent.
         *
         * Examples can be: the child's view is hosted in a dialog, or added to some other
         *  generic host node.
         */
        EXTERNAL
    }

    data class Descriptor(
        val node: Node<*>,
        val viewAttachMode: ViewAttachMode
    )

    companion object {
        internal const val KEY_ROUTER = "node.router"
        internal const val KEY_INTERACTOR = "node.interactor"
        internal const val KEY_VIEW_STATE = "view.state"
    }

    init {
        router.node = this
    }

    val tag: String = this::class.java.name
    internal val children = CopyOnWriteArrayList<Node<*>>()

    internal open var view: V? = null
    protected var parentViewGroup: ViewGroup? = null

    private var savedInstanceState: Bundle? = null
    internal open var savedViewState: SparseArray<Parcelable> = SparseArray()

    internal var isViewAttached: Boolean = false
        private set

    fun getChildren(): List<Node<*>> =
        children.toList()

    open fun attachToView(parentViewGroup: ViewGroup) {
        this.parentViewGroup = parentViewGroup
        isViewAttached = true
        view = createView(parentViewGroup)
        view?.let {
            parentViewGroup.addView(it.androidView)
            it.androidView.restoreHierarchyState(savedViewState)
            interactor.onViewCreated(it)
        }

        router.onAttachView()
    }

    private fun createView(parentViewGroup: ViewGroup): V? =
        viewFactory?.invoke(parentViewGroup)

    internal fun attachChildView(child: Node<*>) {
        if (isViewAttached) {
            child.attachToView(
                // parentViewGroup is guaranteed to be non-null if and only if view is attached
                view?.getParentViewForChild(child.identifier) ?: parentViewGroup!!
            )
        }
    }

    internal fun saveViewState() {
        view?.let {
            it.androidView.saveHierarchyState(savedViewState)
        }
    }

    internal fun detachChildView(child: Node<*>) {
        parentViewGroup?.let {
            child.detachFromView()
        }
    }

    open fun detachFromView() {
        router.onDetachView()

        view?.let {
            parentViewGroup!!.removeView(it.androidView)
            interactor.onViewDestroyed()
        }

        view = null
        isViewAttached = false
        this.parentViewGroup = null
    }

    /**
     * Dispatch back press to the associated interactor.
     *
     * @return TRUE if the interactor handled the back press and no further action is necessary.
     */
    @CallSuper
    open fun handleBackPress(): Boolean {
//        ribRefWatcher.logBreadcrumb("BACKPRESS", null, null)
        return children
                .filter { it.isViewAttached }
                .any { it.handleBackPress() }
            || interactor.handleBackPress()
            || router.popBackStack()
    }

    /**
     * Attaches a child node to this node.
     *
     * @param childNode the [Node] to be attached.
     */
    @MainThread
    internal fun attachChildNode(childNode: Node<*>, bundle: Bundle?) {
        children.add(childNode)
//        ribRefWatcher.logBreadcrumb(
//            "ATTACHED", childNode.javaClass.simpleName, this.javaClass.simpleName
//        )

        childNode.onAttach(bundle)
    }

    /**
     * Detaches the node from this parent. NOTE: No consumers of
     * this API should ever keep a reference to the detached child, leak canary will enforce
     * that it gets garbage collected.
     *
     * @param childNode the [Node] to be detached.
     */
    @MainThread
    internal fun detachChildNode(childNode: Node<*>) {
        children.remove(childNode)

        val interactor = childNode.interactor
//        ribRefWatcher.watchDeletedObject(interactor)
//        ribRefWatcher.logBreadcrumb(
//            "DETACHED", childNode.javaClass.simpleName, this.javaClass.simpleName
//        )

        childNode.onDetach()
    }

    @CallSuper
    open fun onAttach(savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState

        savedViewState = savedInstanceState?.getSparseParcelableArray<Parcelable>(KEY_VIEW_STATE) ?: SparseArray()

        router.onAttach(savedInstanceState?.getBundle(KEY_ROUTER))
        interactor.onAttach(savedInstanceState?.getBundle(KEY_INTERACTOR))
    }

    open fun onDetach() {
        interactor.onDetach()
        router.onDetach()

        for (child in children) {
            detachChildNode(child)
        }
    }

    open fun onSaveInstanceState(outState: Bundle) {
        saveRouterState(outState)
        saveInteractorState(outState)
        saveViewState()
        outState.putSparseParcelableArray(KEY_VIEW_STATE, savedViewState)
    }

    fun onLowMemory() {
        router.onLowMemory()
    }

    private fun saveRouterState(outState: Bundle) {
        Bundle().let {
            router.onSaveInstanceState(it)
            outState.putBundle(KEY_ROUTER, it)
        }
    }

    private fun saveInteractorState(outState: Bundle) {
        Bundle().let {
            interactor.onSaveInstanceState(it)
            outState.putBundle(KEY_INTERACTOR, it)
        }
    }

    fun onStart() {
        interactor.onStart()
        children.forEach { it.onStart() }
    }

    fun onStop() {
        interactor.onStop()
        children.forEach { it.onStop() }
    }

    fun onResume() {
        interactor.onResume()
        children.forEach { it.onResume() }
    }

    fun onPause() {
        interactor.onPause()
        children.forEach { it.onPause() }
    }

    override fun toString(): String =
        "Node@${hashCode()} ($identifier)"
}

