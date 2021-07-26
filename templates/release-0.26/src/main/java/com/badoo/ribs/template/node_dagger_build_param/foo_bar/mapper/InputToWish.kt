package com.badoo.ribs.template.node_dagger_build_param.foo_bar.mapper

import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBar.Input
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.feature.FooBarFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? =
        null
}
