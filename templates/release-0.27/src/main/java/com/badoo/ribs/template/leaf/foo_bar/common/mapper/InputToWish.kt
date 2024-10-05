package com.badoo.ribs.template.leaf.foo_bar.common.mapper

import com.badoo.ribs.template.leaf.foo_bar.common.FooBar.Input
import com.badoo.ribs.template.leaf.foo_bar.common.feature.FooBarFeature.Wish

internal object InputToWish : (Input) -> Wish? {

    override fun invoke(event: Input): Wish? =
        TODO("Implement FooBarInputToWish mapping")
}
