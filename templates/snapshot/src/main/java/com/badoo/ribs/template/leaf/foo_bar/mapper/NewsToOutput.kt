package com.badoo.ribs.template.leaf.foo_bar.mapper

import com.badoo.ribs.template.leaf.foo_bar.FooBar.Output
import com.badoo.ribs.template.leaf.foo_bar.feature.FooBarFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? =
        null
}
