package com.badoo.ribs.core.routing.portal

import android.os.Bundle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Overlay

internal class PortalInteractor(
    savedInstanceState: Bundle?,
    private val router: Router<Configuration, *, Content, Overlay, Nothing>
) : Interactor<Nothing>(
    savedInstanceState = savedInstanceState,
    disposables = null
)
