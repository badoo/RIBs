package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainer

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
