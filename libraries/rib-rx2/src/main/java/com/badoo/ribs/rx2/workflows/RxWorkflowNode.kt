package com.badoo.ribs.rx2.workflows

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single
import kotlin.reflect.KClass

open class RxWorkflowNode<V : RibView>(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<V>?,
    plugins: List<Plugin> = emptyList()
) : Node<V>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
) {

    private val childrenAttachesRelay: PublishRelay<Node<*>> = PublishRelay.create()
    private val detachSignalRelay: BehaviorRelay<Unit> = BehaviorRelay.create()

    protected val childrenAttaches: Observable<Node<*>> = childrenAttachesRelay.hide()
    protected val detachSignal: Observable<Unit> = detachSignalRelay.hide()

    override fun onAttachChildNode(child: Node<*>) {
        super.onAttachChildNode(child)
        childrenAttachesRelay.accept(child)
    }

    override fun onDestroy(isRecreating: Boolean) {
        super.onDestroy(isRecreating)
        detachSignalRelay.accept(Unit)
    }

    /**
     * Executes an action and remains on the same hierarchical level
     *
     * @return the current workflow element
     */
    @Suppress("UNCHECKED_CAST")
    protected fun <T> executeWorkflow(
        action: () -> Unit
    ): Single<T> = Single.defer {
        throwExceptionSingleIfDestroyed<T>() ?: Single.fromCallable {
            action()
            this as T
        }
    }

    /**
     * Executes an action and transitions to another workflow element
     *
     * @param action an action that's supposed to result in the attach of a child (e.g. router.push())
     *
     * @return the child as the expected workflow element, or error if expected child was not found
     */
    protected inline fun <reified T : Any> attachWorkflow(
        noinline action: () -> Unit
    ): Single<T> = attachWorkflow(T::class, action)

    protected fun <T : Any> attachWorkflow(
        clazz: KClass<T>,
        action: () -> Unit
    ): Single<T> = Single.defer {
        throwExceptionSingleIfDestroyed<T>()?.also { return@defer it }
        action()
        val childNodesOfExpectedType = children.filterIsInstance(clazz.java)
        if (childNodesOfExpectedType.isEmpty()) {
            Single.error(
                IllegalStateException(
                    "Expected child of type [${clazz.java}] was not found after executing action. " +
                        "Check that your action actually results in the expected child. " +
                        "Child count: ${children.size}. " +
                        "Last child is: [${children.lastOrNull()}]. " +
                        "All children: $children"
                )
            )
        } else {
            Single.just(childNodesOfExpectedType.last())
        }
    }

    /**
     * Waits until a certain child is attached and returns it as the expected workflow element, or
     * returns it immediately if it's already available.
     *
     * @return the child as the expected workflow element
     */
    protected inline fun <reified T : Any> waitForChildAttached(): Single<T> =
        waitForChildAttached(T::class)

    protected fun <T : Any> waitForChildAttached(
        clazz: KClass<T>,
    ): Single<T> =
        Single.defer {
            throwExceptionSingleIfDestroyed<T>()?.also { return@defer it }
            val childNodesOfExpectedType = children.filterIsInstance(clazz.java)
            if (childNodesOfExpectedType.isEmpty()) {
                childrenAttaches.ofType(clazz.java)?.firstOrError()
            } else {
                Single.just(childNodesOfExpectedType.last())
            }
        }

    private fun <T> throwExceptionSingleIfDestroyed(): Single<T>? =
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            Single.error(IllegalStateException("Node $this is already destroyed, further execution is meaningless"))
        } else {
            null
        }

}
