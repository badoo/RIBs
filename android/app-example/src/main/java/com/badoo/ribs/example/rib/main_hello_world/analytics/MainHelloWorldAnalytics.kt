package com.badoo.ribs.example.rib.main_hello_world.analytics

import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldView
import io.reactivex.functions.Consumer

internal object MainHelloWorldAnalytics : Consumer<MainHelloWorldAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: MainHelloWorldView.Event) : Event()
    }

    override fun accept(event: MainHelloWorldAnalytics.Event) {
        // TODO Implement tracking
    }
}
