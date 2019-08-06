package com.badoo.ribs.template.rib_with_view.foo_bar.mapper

import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarView.ViewModel
import com.badoo.ribs.template.rib_with_view.foo_bar.feature.FooBarFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel =
        TODO("Implement StateToViewModel mapping")
}
