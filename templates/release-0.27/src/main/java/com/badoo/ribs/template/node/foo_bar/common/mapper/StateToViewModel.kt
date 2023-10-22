package com.badoo.ribs.template.node.foo_bar.common.mapper

import com.badoo.ribs.template.node.foo_bar.common.view.FooBarView.ViewModel
import com.badoo.ribs.template.node.foo_bar.common.feature.FooBarFeature.State

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel =
        TODO("Implement StateToViewModel mapping")
}
