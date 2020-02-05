package com.badoo.ribs.core.routing.portal

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Overlay

internal class PortalInteractor(
    buildContext: BuildContext<*>,
    router: Router<Configuration, *, Content, Overlay, Nothing>
) : Interactor<Configuration, Content, Overlay, Nothing>(
    buildContext = buildContext,
    router = router,
    disposables = null
)
