package com.badoo.ribs.samples.communication.menu_example.rib.menu_example.routing

import com.badoo.ribs.samples.communication.menu_example.rib.child1.Child1
import com.badoo.ribs.samples.communication.menu_example.rib.child1.Child1Builder
import com.badoo.ribs.samples.communication.menu_example.rib.child2.Child2
import com.badoo.ribs.samples.communication.menu_example.rib.child2.Child2Builder
import com.badoo.ribs.samples.communication.menu_example.rib.child3.Child3
import com.badoo.ribs.samples.communication.menu_example.rib.child3.Child3Builder
import com.badoo.ribs.samples.communication.menu_example.rib.menu_example.MenuExample
import com.badoo.ribs.samples.communication.menu_example.rib.menu.Menu
import com.badoo.ribs.samples.communication.menu_example.rib.menu.MenuBuilder

internal class MenuExampleChildBuilders(
    dependency: MenuExample.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val menu: MenuBuilder = MenuBuilder(subtreeDeps)
    val child1: Child1Builder = Child1Builder(subtreeDeps)
    val child2: Child2Builder = Child2Builder(subtreeDeps)
    val child3: Child3Builder = Child3Builder(subtreeDeps)

    class SubtreeDependency(
        dependency: MenuExample.Dependency
    ) : MenuExample.Dependency by dependency,
        Menu.Dependency,
        Child1.Dependency,
        Child2.Dependency,
        Child3.Dependency
}
