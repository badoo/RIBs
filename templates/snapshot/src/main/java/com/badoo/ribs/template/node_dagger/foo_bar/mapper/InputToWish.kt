package com.badoo.ribs.template.node_dagger.foo_bar.mapper

import com.badoo.ribs.template.node_dagger.foo_bar.FooBar.Input
import com.badoo.ribs.template.node_dagger.foo_bar.feature.FooBarFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? =
        TODO("Implement FooBarInputToWish mapping")
}
