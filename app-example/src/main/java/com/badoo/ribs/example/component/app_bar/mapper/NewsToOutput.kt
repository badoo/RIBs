package com.badoo.ribs.example.component.app_bar.mapper

import com.badoo.ribs.example.component.app_bar.AppBar.Output
import com.badoo.ribs.example.component.app_bar.feature.AppBarFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? =
        TODO("Implement AppBarNewsToOutput mapping")
}
