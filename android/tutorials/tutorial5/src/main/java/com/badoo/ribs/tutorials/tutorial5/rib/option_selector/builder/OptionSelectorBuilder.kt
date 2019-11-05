package com.badoo.ribs.tutorials.tutorial5.rib.option_selector.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelector
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelectorView

class OptionSelectorBuilder(
    override val dependency: OptionSelector.Dependency
) : Builder<OptionSelector.Dependency>() {

    fun build(savedInstanceState: Bundle?): Node<OptionSelectorView> =
        DaggerOptionSelectorComponent
            .factory()
            .create(
                savedInstanceState = savedInstanceState,
                dependency = dependency,
                customisation = OptionSelector.Customisation()
            )
            .node()
}
