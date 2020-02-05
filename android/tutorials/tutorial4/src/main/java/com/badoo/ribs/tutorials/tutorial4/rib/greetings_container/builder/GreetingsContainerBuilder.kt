package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainer

class GreetingsContainerBuilder(
    override val dependency: GreetingsContainer.Dependency
) : Builder<GreetingsContainer.Dependency, Nothing?, Node<Nothing>>() {

    override fun build(buildContext: BuildContext<Nothing?>): Node<Nothing> {
        val component = DaggerGreetingsContainerComponent.factory()
            .create(
                dependency = dependency,
                buildContext = buildContext
            )

        return component.node()
    }
}
