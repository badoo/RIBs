package com.badoo.ribs.portal.rx2

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.portal.PortalRouter.Configuration
import com.badoo.ribs.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.rx2.workflows.RxWorkflowNode
import io.reactivex.Single

internal class Rx2PortalNode(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>,
    plugins: List<Plugin>
) : RxWorkflowNode<Nothing>(
    buildParams = buildParams,
    viewFactory = null,
    plugins = plugins
), Rx2Portal {

    override fun showDefault(): Single<Rib> =
        attachWorkflow {
            backStack.push(Content.Default)
        }

    override fun showInPortal(ancestryInfo: AncestryInfo): Single<Rib> =
        attachWorkflow {
            backStack.push(Content.Portal(ancestryInfo.routingChain))
        }
}
