package com.badoo.ribs.example.rib.main_foo_bar.mapper

import com.badoo.ribs.example.rib.main_foo_bar.MainFooBarView
import com.badoo.ribs.example.rib.main_foo_bar.feature.MainFooBarFeature

internal object ViewEventToWish : (MainFooBarView.Event) -> MainFooBarFeature.Wish? {

    override fun invoke(event: MainFooBarView.Event): MainFooBarFeature.Wish? =
        TODO("Implement FooBarViewEventToWish mapping")
}
