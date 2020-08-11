package com.badoo.ribs.portal

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.portal.PortalRouter.Configuration
import com.badoo.ribs.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.workflows.rx.RxWorkflowNode
import io.reactivex.Single

internal class RxPortalNode(
    buildParams: BuildParams<*>,
    plugins: List<Plugin>,
    private val backStack: BackStackFeature<Configuration>
) : RxWorkflowNode<Nothing>(
    buildParams = buildParams,
    viewFactory = null,
    plugins = plugins
), RxPortal {

    override fun showDefault(): Single<Rib> =
        attachWorkflow {
            backStack.push(Content.Default)
        }

    override fun showInPortal(ancestryInfo: AncestryInfo): Single<Rib> =
        attachWorkflow {
            backStack.push(Content.Portal(ancestryInfo.routingChain))
        }
}
