package com.badoo.ribs.template.leaf_dagger.foo_bar.mapper

import com.badoo.ribs.template.leaf_dagger.foo_bar.FooBarView.Event
import com.badoo.ribs.template.leaf_dagger.foo_bar.feature.FooBarFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? =
        null
}
