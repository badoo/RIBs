package com.badoo.ribs.sandbox.rib.foo_bar.analytics

import com.badoo.ribs.sandbox.rib.foo_bar.FooBarView
import io.reactivex.rxjava3.functions.Consumer

internal object FooBarAnalytics : Consumer<FooBarAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: FooBarView.Event) : Event()
    }

    override fun accept(event: Event) {
        // TODO Implement tracking
    }
}
