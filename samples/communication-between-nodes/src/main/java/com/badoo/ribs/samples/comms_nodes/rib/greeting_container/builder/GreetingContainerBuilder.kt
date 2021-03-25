package com.badoo.ribs.samples.comms_nodes.rib.greeting_container.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.GreetingContainer

class GreetingContainerBuilder : SimpleBuilder<Node<Nothing>>() {

    override fun build(buildParams: BuildParams<Nothing?>) =
        Node<Nothing>(
            buildParams = buildParams,
            viewFactory = null,
            plugins = listOf()
        )
}