package com.badoo.ribs.template.leaf_dagger.foo_bar.mapper

import com.badoo.ribs.template.leaf_dagger.foo_bar.FooBarView.ViewModel
import com.badoo.ribs.template.leaf_dagger.foo_bar.feature.FooBarFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel =
        ViewModel()
}
