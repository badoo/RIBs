package com.badoo.ribs.example.rib.portal_full_screen

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Node

class PortalFullScreenNode(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> PortalFullScreenView?)?,
    private val router: PortalFullScreenRouter,
    private val interactor: PortalFullScreenInteractor
) : Node<PortalFullScreenView>(
    savedInstanceState = savedInstanceState,
    identifier = object : PortalFullScreen {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), PortalFullScreen.Workflow
