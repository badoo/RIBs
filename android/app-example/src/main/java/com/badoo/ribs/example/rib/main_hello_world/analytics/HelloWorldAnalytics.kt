package com.badoo.ribs.example.rib.main_hello_world.analytics

import com.badoo.ribs.example.rib.main_hello_world.HelloWorldView
import io.reactivex.functions.Consumer

internal object HelloWorldAnalytics : Consumer<HelloWorldAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: HelloWorldView.Event) : Event()
    }

    override fun accept(event: HelloWorldAnalytics.Event) {
        // TODO Implement tracking
    }
}
