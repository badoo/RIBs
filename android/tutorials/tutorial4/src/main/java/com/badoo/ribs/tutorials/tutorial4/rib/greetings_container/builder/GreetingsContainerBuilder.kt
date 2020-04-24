package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainer

class GreetingsContainerBuilder(
    private val dependency: GreetingsContainer.Dependency
) : SimpleBuilder<Node<Nothing>>() {

    override fun build(buildParams: BuildParams<Nothing?>): Node<Nothing> {
        val component = DaggerGreetingsContainerComponent.factory()
            .create(
                dependency = dependency,
                buildParams = buildParams
            )

        return component.node()
    }
}
