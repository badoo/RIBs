package com.badoo.ribs.example.rib.lorem_ipsum.mapper

import com.badoo.ribs.example.rib.lorem_ipsum.feature.LoremIpsumFeature.Wish
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView.Event

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? =
        TODO("Implement LoremIpsumViewEventToWish mapping")
}
