package com.badoo.ribs.example.rib.lorem_ipsum.mapper

import com.badoo.ribs.example.rib.lorem_ipsum.analytics.LoremIpsumAnalytics
import com.badoo.ribs.example.rib.lorem_ipsum.analytics.LoremIpsumAnalytics.Event.ViewEvent
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView.Event

internal object ViewEventToAnalyticsEvent : (Event) -> LoremIpsumAnalytics.Event? {

    override fun invoke(event: Event): LoremIpsumAnalytics.Event? =
        ViewEvent(event)
}
