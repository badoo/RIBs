package com.badoo.ribs.core.routing.portal

import android.os.Bundle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Content

internal class PortalInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, *, Content, Nothing, Nothing>
) : Interactor<Configuration, Content, Nothing, Nothing>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
)
