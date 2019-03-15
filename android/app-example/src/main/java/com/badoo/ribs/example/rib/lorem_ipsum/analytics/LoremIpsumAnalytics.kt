package com.badoo.ribs.example.rib.lorem_ipsum.analytics

import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView
import io.reactivex.functions.Consumer

internal object LoremIpsumAnalytics : Consumer<LoremIpsumAnalytics.Event> {

    sealed class Event {
        data class ViewEvent(val event: LoremIpsumView.Event) : Event()
    }

    override fun accept(event: LoremIpsumAnalytics.Event) {
        // TODO Implement tracking
    }
}
