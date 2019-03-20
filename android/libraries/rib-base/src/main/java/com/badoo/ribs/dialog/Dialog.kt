package com.badoo.ribs.dialog

import com.badoo.ribs.core.Node
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

abstract class Dialog<Event : Any> private constructor(
    factory: Dialog<Event>.() -> Unit,
    private val events: PublishRelay<Event>
) : ObservableSource<Event> by events {
    var title: String? = null
    var message: String? = null
    internal var buttons: ButtonsConfig<Event>? = null
    private var ribFactory: (() -> Node<*>)? = null
    internal var rib: Node<*>? = null

    constructor(factory: Dialog<Event>.() -> Unit) : this(
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

    fun buttons(factory: ButtonsConfig<Event>.() -> Unit) {
        this.buttons = ButtonsConfig(factory)
    }

    class ButtonsConfig<Event : Any>(
        factory: ButtonsConfig<Event>.() -> Unit
    ) {
        internal var positive: ButtonConfig<Event>? = null
        internal var negative: ButtonConfig<Event>? = null
        internal var neutral: ButtonConfig<Event>? = null

        init {
            factory()
        }

        fun positive(title: String, onClickEvent: Event) {
            positive = ButtonConfig(title, onClickEvent)
        }

        fun negative(title: String, onClickEvent: Event) {
            negative = ButtonConfig(title, onClickEvent)
        }

        fun neutral(title: String, onClickEvent: Event) {
            neutral = ButtonConfig(title, onClickEvent)
        }

        data class ButtonConfig<Event : Any>(
            var title: String? = null,
            var onClickEvent: Event? = null
        )
    }

    fun publish(event: Event) {
        events.accept(event)
    }

    sealed class Event {
        object Positive : Event()
        object Negative : Event()
        object Neutral : Event()
    }

    fun createRibs(): List<Node<*>> =
        ribFactory?.let { factory ->
            listOf(
                factory.invoke().also {
                    rib = it
                }
            )
        } ?: emptyList()
}
