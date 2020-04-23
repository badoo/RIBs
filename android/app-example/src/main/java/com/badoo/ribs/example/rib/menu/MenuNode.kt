package com.badoo.ribs.example.rib.menu

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams

class MenuNode(
    buildParams: BuildParams<Nothing?>,
    customisation: Menu.Customisation,
    interactor: MenuInteractor
): Node<MenuView>(
    buildParams = buildParams,
    viewFactory = customisation.viewFactory(null),
    router = null,
    interactor = interactor
)
