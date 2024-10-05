package com.badoo.ribs.template.leaf.foo_bar.common.analytics

import com.badoo.ribs.template.leaf.foo_bar.common.view.FooBarView
import io.reactivex.functions.Consumer

internal object FooBarAnalytics : Consumer<FooBarAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: FooBarView.Event) : Event()
    }

    override fun accept(event: Event) {
        // TODO Implement tracking
    }
}
