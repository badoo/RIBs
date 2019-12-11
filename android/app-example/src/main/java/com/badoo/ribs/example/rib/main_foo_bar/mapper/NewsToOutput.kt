package com.badoo.ribs.example.rib.main_foo_bar.mapper

import com.badoo.ribs.example.rib.main_foo_bar.MainFooBar
import com.badoo.ribs.example.rib.main_foo_bar.feature.MainFooBarFeature

internal object NewsToOutput : (MainFooBarFeature.News) -> MainFooBar.Output? {

    override fun invoke(news: MainFooBarFeature.News): MainFooBar.Output? =
        TODO("Implement FooBarNewsToOutput mapping")
}
