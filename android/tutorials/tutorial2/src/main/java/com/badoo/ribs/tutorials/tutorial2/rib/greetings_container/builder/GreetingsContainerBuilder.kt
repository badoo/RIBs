package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.builder

import com.badoo.ribs.core.BuildParams
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainer

class GreetingsContainerBuilder(
    override val dependency: GreetingsContainer.Dependency
) : Builder<GreetingsContainer.Dependency, Nothing?, Node<Nothing>>() {

    override fun build(buildParams: BuildParams<Nothing?>): Node<Nothing> {
        val component = DaggerGreetingsContainerComponent.factory()
            .create(
                dependency = dependency,
                buildContext = buildParams
            )

        return component.node()
    }
}
