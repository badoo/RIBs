package com.badoo.ribs.core.routing.portal

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.configuration.feature.operation.push
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Content
import io.reactivex.Single

class PortalNode internal constructor(
    savedInstanceState: Bundle?,
    private val router: PortalRouter,
    interactor: PortalInteractor
) : Node<Nothing>(
    savedInstanceState = savedInstanceState,
    identifier = object : Portal {},
    viewFactory = null,
    router = router,
    interactor = interactor
), Portal.Workflow {

    override fun showDefault(): Single<Node<*>> =
        attachWorkflow {
            router.push(Content.Default)
        }

    override fun showInPortal(ancestryInfo: AncestryInfo): Single<Node<*>> =
        attachWorkflow {
            router.push(Content.Portal(ancestryInfo.configurationChain))
        }
}
