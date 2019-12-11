package com.badoo.ribs.example.rib.main_foo_bar.analytics

import com.badoo.ribs.example.rib.main_foo_bar.FooBarView
import io.reactivex.functions.Consumer

internal object FooBarAnalytics : Consumer<FooBarAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: FooBarView.Event) : Event()
    }

    override fun accept(event: FooBarAnalytics.Event) {
        // TODO Implement tracking
    }
}
