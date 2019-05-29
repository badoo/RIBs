package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerView

class GreetingsContainerBuilder(
    override val dependency: GreetingsContainer.Dependency
) : Builder<GreetingsContainer.Dependency>() {

    fun build(): Node<GreetingsContainerView> {
        val customisation = GreetingsContainer.Customisation()
        val component = DaggerGreetingsContainerComponent.builder()
            .dependency(dependency)
            .customisation(customisation)
            .build()

        return component.node()
    }
}
