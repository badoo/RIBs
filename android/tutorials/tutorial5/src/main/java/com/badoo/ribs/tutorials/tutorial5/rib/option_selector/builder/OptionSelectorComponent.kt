package com.badoo.ribs.tutorials.tutorial5.rib.option_selector.builder

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelector
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelectorView
import dagger.BindsInstance

@OptionSelectorScope
@dagger.Component(
    modules = [OptionSelectorModule::class],
    dependencies = [OptionSelector.Dependency::class]
)
internal interface OptionSelectorComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: OptionSelector.Dependency,
            @BindsInstance savedInstanceState: Bundle?,
            @BindsInstance customisation: OptionSelector.Customisation
        ): OptionSelectorComponent
    }

    fun node(): Node<OptionSelectorView>
}
