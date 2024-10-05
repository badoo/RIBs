package com.badoo.ribs.template.node.foo_bar.common.mapper

import com.badoo.ribs.template.node.foo_bar.common.FooBar.Output
import com.badoo.ribs.template.node.foo_bar.common.feature.FooBarFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? =
        TODO("Implement FooBarNewsToOutput mapping")
}
