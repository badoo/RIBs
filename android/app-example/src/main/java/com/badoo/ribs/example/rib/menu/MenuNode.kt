package com.badoo.ribs.example.rib.menu

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams

class MenuNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: (ViewGroup) -> MenuView,
    interactor: MenuInteractor
): Node<MenuView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = null,
    interactor = interactor
)
