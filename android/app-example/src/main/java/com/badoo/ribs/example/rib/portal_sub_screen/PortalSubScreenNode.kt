package com.badoo.ribs.example.rib.portal_sub_screen

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Node

class PortalSubScreenNode(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> PortalSubScreenView?)?,
    private val router: PortalSubScreenRouter,
    private val interactor: PortalSubScreenInteractor
) : Node<PortalSubScreenView>(
    savedInstanceState = savedInstanceState,
    identifier = object : PortalSubScreen {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), PortalSubScreen.Workflow {

}
