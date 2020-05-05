package com.badoo.ribs.example.rib.menu

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.example.rib.menu.feature.MenuFeature

class MenuBuilder(
    private val dependency: Menu.Dependency
) : SimpleBuilder<Menu>() {

    override fun build(buildParams: BuildParams<Nothing?>): Menu {
        val customisation = buildParams.getOrDefault(Menu.Customisation())
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
