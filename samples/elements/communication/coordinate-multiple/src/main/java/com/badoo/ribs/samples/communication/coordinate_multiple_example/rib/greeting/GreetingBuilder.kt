package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting.Greeting.Customisation

class GreetingBuilder(
    private val dependency: Greeting.Dependency
) : SimpleBuilder<Greeting>() {

    override fun build(buildParams: BuildParams<Nothing?>): Greeting {
        val presenter = GreetingPresenterImpl()
        val viewDependency = object : GreetingView.Dependency {
            override val presenter: GreetingPresenter
                get() = presenter
        }
        val customisation = buildParams.getOrDefault(Customisation())

        return GreetingNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory.invoke(viewDependency),
            plugins = listOf(presenter)
        )
    }
}
