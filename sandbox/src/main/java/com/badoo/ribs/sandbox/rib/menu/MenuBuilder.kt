package com.badoo.ribs.sandbox.rib.menu

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.menu.feature.MenuFeature

class MenuBuilder(
    private val dependency: Menu.Dependency
) : SimpleBuilder<Menu>() {

    override fun build(buildParams: BuildParams<Nothing?>): Menu {
        val customisation = buildParams.getOrDefault(Menu.Customisation())
        val feature = MenuFeature()
        val interactor = MenuInteractor(
            buildParams = buildParams,
            feature = feature
        )

        return MenuNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory.invoke(null),
            plugins = listOf(interactor)
        )
    }
}
