package com.badoo.ribs.core.routing.portal

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Overlay

internal class PortalInteractor(
    buildParams: BuildParams<Nothing?>,
    private val router: Router<Configuration, *, Content, Overlay, Nothing>
) : Interactor<Portal, Nothing>(
    buildParams = buildParams,
    disposables = null
)
