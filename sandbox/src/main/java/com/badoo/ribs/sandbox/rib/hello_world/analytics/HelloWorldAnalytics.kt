package com.badoo.ribs.sandbox.rib.hello_world.analytics

import com.badoo.ribs.sandbox.rib.hello_world.HelloWorldView
import io.reactivex.rxjava3.functions.Consumer

internal object HelloWorldAnalytics : Consumer<HelloWorldAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: HelloWorldView.Event) : Event()
    }

    override fun accept(event: Event) {
        // TODO Implement tracking
    }
}
