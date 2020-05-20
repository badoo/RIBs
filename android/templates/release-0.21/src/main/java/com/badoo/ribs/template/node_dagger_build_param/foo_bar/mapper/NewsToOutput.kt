package com.badoo.ribs.template.node_dagger_build_param.foo_bar.mapper

import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBar.Output
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.feature.FooBarFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? =
        TODO("Implement FooBarNewsToOutput mapping")
}
