package com.badoo.ribs.template.node_dagger_build_param.foo_bar.mapper

import com.badoo.ribs.template.node_dagger_build_param.foo_bar.FooBarView.Event
import com.badoo.ribs.template.node_dagger_build_param.foo_bar.feature.FooBarFeature.Wish

internal object ViewEventToWish : (Event) -> Wish? {

    override fun invoke(event: Event): Wish? =
        null
}
