package com.badoo.ribs.example.rib.menu.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.MenuView

class MenuBuilder(
    dependency: Menu.Dependency
) : Builder<Menu.Dependency>() {

    override val dependency : Menu.Dependency = object : Menu.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Menu::class)
    }

    fun build(savedInstanceState: Bundle?): Node<MenuView> =
        DaggerMenuComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Menu.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
