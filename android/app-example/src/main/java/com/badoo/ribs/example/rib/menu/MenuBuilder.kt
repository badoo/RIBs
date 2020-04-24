package com.badoo.ribs.example.rib.menu

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.menu.feature.MenuFeature

class MenuBuilder(
    dependency: Menu.Dependency
) : SimpleBuilder<MenuNode>(
    rib = object : Menu {}
) {

    private val dependency : Menu.Dependency = object : Menu.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Menu::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): MenuNode {
        val customisation = dependency.getOrDefault(Menu.Customisation())
        val feature = MenuFeature()
        val interactor = MenuInteractor(
            buildParams = buildParams,
            input = dependency.menuInput(),
            output = dependency.menuOutput(),
            feature = feature
        )

        return MenuNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory.invoke(null),
            interactor = interactor
        )
    }
}
