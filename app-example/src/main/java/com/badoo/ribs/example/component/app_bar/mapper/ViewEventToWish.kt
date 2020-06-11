package com.badoo.ribs.example.component.app_bar.mapper

import com.badoo.ribs.example.component.app_bar.AppBarView.Event
import com.badoo.ribs.example.component.app_bar.feature.AppBarFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? =
        TODO("Implement AppBarViewEventToWish mapping")
}
