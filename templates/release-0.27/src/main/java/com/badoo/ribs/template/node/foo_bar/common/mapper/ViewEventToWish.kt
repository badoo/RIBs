package com.badoo.ribs.template.node.foo_bar.common.mapper

import com.badoo.ribs.template.node.foo_bar.common.view.FooBarView.Event
import com.badoo.ribs.template.node.foo_bar.common.feature.FooBarFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? =
        TODO("Implement FooBarViewEventToWish mapping")
}
