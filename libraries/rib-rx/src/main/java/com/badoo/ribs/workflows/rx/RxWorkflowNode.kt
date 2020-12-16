package com.badoo.ribs.workflows.rx

import androidx.annotation.VisibleForTesting
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Single

open class RxWorkflowNode<V : RibView>(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> V?)?,
    plugins: List<Plugin> = emptyList()
) : Node<V>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
) {

    private val childrenAttachesRelay: PublishRelay<Node<*>>? = PublishRelay.create()
    val childrenAttaches: Observable<Node<*>>? = childrenAttachesRelay?.hide()
    val detachSignal: BehaviorRelay<Unit> = BehaviorRelay.create()

    override fun onAttachChildNode(child: Node<*>) {
        super.onAttachChildNode(child)
        childrenAttachesRelay?.accept(child)
    }

    override fun onDestroy(isRecreating: Boolean) {
        super.onDestroy(isRecreating)
        detachSignal.accept(Unit)
    }

    /**
     * Executes an action and remains on the same hierarchical level
     *
     * @return the current workflow element
     */
    protected inline fun <reified T> executeWorkflow(
        crossinline action: () -> Unit
    ): Single<T> = Single.fromCallable {
        action()
        this as T
    }
        .takeUntil(detachSignal.firstOrError())

    @VisibleForTesting
    internal inline fun <reified T> executeWorkflowInternal(
        crossinline action: () -> Unit
    ): Single<T> = executeWorkflow(action)

    /**
     * Executes an action and transitions to another workflow element
     *
     * @param action an action that's supposed to result in the attach of a child (e.g. router.push())
     *
     * @return the child as the expected workflow element, or error if expected child was not found
     */
    @SuppressWarnings("LongMethod")
    protected inline fun <reified T> attachWorkflow(
        crossinline action: () -> Unit
    ): Single<T> = Single.fromCallable {
        action()
        val childNodesOfExpectedType = children.filterIsInstance<T>()
        if (childNodesOfExpectedType.isEmpty()) {
            Single.error<T>(
                IllegalStateException(
                    "Expected child of type [${T::class.java}] was not found after executing action. " +
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
        .flatMap { it }
        .takeUntil(detachSignal.firstOrError())

    @VisibleForTesting
    internal inline fun <reified T> attachWorkflowInternal(
        crossinline action: () -> Unit
    ): Single<T> = attachWorkflow(action)

    /**
     * Waits until a certain child is attached and returns it as the expected workflow element, or
     * returns it immediately if it's already available.
     *
     * @return the child as the expected workflow element
     */
    protected inline fun <reified T> waitForChildAttached(): Single<T> =
        Single.fromCallable {
            val childNodesOfExpectedType = children.filterIsInstance<T>()
            if (childNodesOfExpectedType.isEmpty()) {
                childrenAttaches?.ofType(T::class.java)?.firstOrError()
            } else {
                Single.just(childNodesOfExpectedType.last())
            }
        }
            .flatMap { it }
            .takeUntil(detachSignal.firstOrError())

    @VisibleForTesting
    internal inline fun <reified T> waitForChildAttachedInternal(): Single<T> =
        waitForChildAttached()
}
