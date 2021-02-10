package com.badoo.ribs.samples.comms_nodes_1.rib.menu

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class MenuBuilder(
    private val dependency: Menu.Dependency
) : SimpleBuilder<Menu>() {

    override fun build(buildParams: BuildParams<Nothing?>): Menu {
        return MenuNode(
            buildParams = buildParams,
            viewFactory = MenuViewImpl.Factory().invoke(deps = null)
        )
    }
}
