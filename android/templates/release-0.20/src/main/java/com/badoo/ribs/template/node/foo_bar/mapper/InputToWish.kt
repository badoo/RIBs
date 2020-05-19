package com.badoo.ribs.template.node.foo_bar.mapper

import com.badoo.ribs.template.node.foo_bar.FooBar.Input
import com.badoo.ribs.template.node.foo_bar.feature.FooBarFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? =
        TODO("Implement FooBarInputToWish mapping")
}
