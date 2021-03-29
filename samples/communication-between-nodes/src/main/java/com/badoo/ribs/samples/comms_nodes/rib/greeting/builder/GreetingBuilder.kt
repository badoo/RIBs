package com.badoo.ribs.samples.comms_nodes.rib.greeting.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.comms_nodes.rib.greeting.Greeting
import com.badoo.ribs.samples.comms_nodes.rib.greeting.GreetingNode
import com.badoo.ribs.samples.comms_nodes.rib.greeting.GreetingPresenter
import com.badoo.ribs.samples.comms_nodes.rib.greeting.GreetingPresenterImpl
import com.badoo.ribs.samples.comms_nodes.rib.greeting.GreetingView

class GreetingBuilder : SimpleBuilder<Greeting>() {

    override fun build(buildParams: BuildParams<Nothing?>): Greeting {
        val presenter = GreetingPresenterImpl()
        val viewDependency = object : GreetingView.Dependency {
            override val presenter: GreetingPresenter
                get() = presenter
        }

        return GreetingNode(
            buildParams = buildParams,
            viewFactory = Greeting.Customisation().viewFactory.invoke(viewDependency),
            plugins = listOf(presenter)
        )
    }
}
