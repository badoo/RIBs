package com.badoo.ribs.tutorials.tutorial5.rib.greetings_container.builder

import android.os.Bundle
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial5.rib.greetings_container.GreetingsContainer

class GreetingsContainerBuilder(
    override val dependency: GreetingsContainer.Dependency
) : Builder<GreetingsContainer.Dependency>() {

    fun build(buildParams: BuildParams<Nothing?>): Node<Nothing> {
        val component = DaggerGreetingsContainerComponent.factory()
            .create(
                dependency = dependency,
                savedInstanceState = savedInstanceState
            )

        return component.node()
    }
}
