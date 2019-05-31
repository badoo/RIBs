package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainer

class GreetingsContainerBuilder(
    override val dependency: GreetingsContainer.Dependency
) : Builder<GreetingsContainer.Dependency>() {

    fun build(): Node<Nothing> {
        val component = DaggerGreetingsContainerComponent.builder()
            .dependency(dependency)
            .build()

        return component.node()
    }
}
