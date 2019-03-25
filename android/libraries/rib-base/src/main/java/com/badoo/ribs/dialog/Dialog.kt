package com.badoo.ribs.dialog

import com.badoo.ribs.core.Node
import com.badoo.ribs.dialog.Dialog.CancellationPolicy.NonCancellable
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

abstract class Dialog<T : Any> private constructor(
    factory: Dialog<T>.() -> Unit,
    private val events: PublishRelay<T>
) : ObservableSource<T> by events {
    var title: String? = null
    var message: String? = null
    var cancellationPolicy: CancellationPolicy<T> = NonCancellable()
    internal var buttons: ButtonsConfig<T>? = null
    private var ribFactory: (() -> Node<*>)? = null
    internal var rib: Node<*>? = null

    constructor(factory: Dialog<T>.() -> Unit) : this(
        factory,
        PublishRelay.create()
    )

    init {
        factory()
    }

    fun title(title: String) {
        this.title = title
    }

    fun ribFactory(ribFactory: () -> Node<*>) {
        this.ribFactory = ribFactory
    }

    fun buttons(factory: ButtonsConfig<T>.() -> Unit) {
        this.buttons = ButtonsConfig(factory)
    }

    class ButtonsConfig<T : Any>(
        factory: ButtonsConfig<T>.() -> Unit
    ) {
        internal var positive: ButtonConfig<T>? = null
        internal var negative: ButtonConfig<T>? = null
        internal var neutral: ButtonConfig<T>? = null

        init {
            factory()
        }

        fun positive(title: String, onClickEvent: T) {
            positive = ButtonConfig(title, onClickEvent)
        }

        fun negative(title: String, onClickEvent: T) {
            negative = ButtonConfig(title, onClickEvent)
        }

        fun neutral(title: String, onClickEvent: T) {
            neutral = ButtonConfig(title, onClickEvent)
        }

        data class ButtonConfig<T : Any>(
            var title: String? = null,
            var onClickEvent: T? = null
        )
    }

    fun publish(event: T) {
        events.accept(event)
    }

    fun buildNodes(): List<Node<*>> =
        ribFactory?.let { factory ->
            listOf(
                factory.invoke().also {
                    rib = it
                }
            )
        } ?: emptyList()

    sealed class Event {
        object Positive : Event()
        object Negative : Event()
        object Neutral : Event()
        object Cancelled : Event()
    }

    sealed class CancellationPolicy<T : Any> {
        class NonCancellable<T : Any> : CancellationPolicy<T>()
        data class Cancellable<T : Any>(
            val event: T,
            val cancelOnTouchOutside: Boolean
        ) : CancellationPolicy<T>()
    }
}
