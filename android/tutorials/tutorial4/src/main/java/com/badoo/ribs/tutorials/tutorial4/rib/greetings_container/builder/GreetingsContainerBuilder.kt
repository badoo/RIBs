package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainerView

class GreetingsContainerBuilder(dependency: GreetingsContainer.Dependency) :
    Builder<GreetingsContainer.Dependency>(dependency) {

    fun build(): Node<GreetingsContainerView> {
        val customisation = dependency.ribCustomisation().get(GreetingsContainer.Customisation::class) ?: GreetingsContainer.Customisation()
        val component = DaggerGreetingsContainerComponent.builder()
            .dependency(dependency)
            .customisation(customisation)
            .build()

        return component.node()
    }
}
