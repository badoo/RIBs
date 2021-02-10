package com.badoo.ribs.samples.comms_nodes_1.rib.container.routing

import com.badoo.ribs.samples.comms_nodes_1.rib.container.Container
import com.badoo.ribs.samples.comms_nodes_1.rib.menu.Menu
import com.badoo.ribs.samples.comms_nodes_1.rib.menu.MenuBuilder

internal class ContainerChildBuilders(
    dependency: Container.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val menu = MenuBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: Container.Dependency
    ) : Container.Dependency by dependency,
        Menu.Dependency
}
