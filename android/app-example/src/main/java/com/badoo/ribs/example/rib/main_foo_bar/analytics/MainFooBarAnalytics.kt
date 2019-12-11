package com.badoo.ribs.example.rib.main_foo_bar.analytics

import com.badoo.ribs.example.rib.main_foo_bar.MainFooBarView
import io.reactivex.functions.Consumer

internal object MainFooBarAnalytics : Consumer<MainFooBarAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: MainFooBarView.Event) : Event()
    }

    override fun accept(event: MainFooBarAnalytics.Event) {
        // TODO Implement tracking
    }
}
