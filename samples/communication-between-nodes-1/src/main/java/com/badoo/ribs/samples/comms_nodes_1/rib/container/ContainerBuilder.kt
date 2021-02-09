@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.samples.comms_nodes_1.rib.container

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ContainerBuilder(
    private val dependency: Container.Dependency
) : SimpleBuilder<Container>() {

    override fun build(buildParams: BuildParams<Nothing?>): Container {
        return ContainerNode(
            buildParams = BuildParams.Empty(),
            viewFactory = ContainerViewImpl.Factory().invoke(deps = null)
        )
    }
}
