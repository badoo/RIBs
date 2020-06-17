package com.badoo.ribs.sandbox.rib.switcher.routing

import com.badoo.ribs.sandbox.rib.blocker.Blocker
import com.badoo.ribs.sandbox.rib.blocker.BlockerBuilder
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeaf
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafBuilder
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleBuilder
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarBuilder
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorldBuilder
import com.badoo.ribs.sandbox.rib.menu.Menu
import com.badoo.ribs.sandbox.rib.menu.MenuBuilder
import com.badoo.ribs.sandbox.rib.switcher.Switcher

internal open class SwitcherChildBuilders(
    dependency: Switcher.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val menu = MenuBuilder(subtreeDeps)
    val helloWorld = HelloWorldBuilder(subtreeDeps)
    val fooBar = FooBarBuilder(subtreeDeps)
    val dialogExample =
        DialogExampleBuilder(
            subtreeDeps
        )
    val blocker = BlockerBuilder(subtreeDeps)
    val composeLeaf = ComposeLeafBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: Switcher.Dependency
    ) : Switcher.Dependency by dependency,
        Menu.Dependency,
        HelloWorld.Dependency,
        FooBar.Dependency,
        DialogExample.Dependency,
        Blocker.Dependency,
        ComposeLeaf.Dependency {

    }
}
