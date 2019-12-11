package com.badoo.ribs.example.rib.main_foo_bar.mapper

import com.badoo.ribs.example.rib.main_foo_bar.MainFooBar
import com.badoo.ribs.example.rib.main_foo_bar.feature.MainFooBarFeature

internal object InputToWish : (MainFooBar.Input) -> MainFooBarFeature.Wish? {

    override fun invoke(event: MainFooBar.Input): MainFooBarFeature.Wish? =
        TODO("Implement FooBarInputToWish mapping")
}
