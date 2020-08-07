package com.badoo.ribs.portal

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.state.rx2
import com.badoo.ribs.portal.PortalRouter.Configuration
import com.badoo.ribs.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.routing.source.backstack.operation.push
import io.reactivex.Single

internal class PortalNode(
    buildParams: BuildParams<*>,
    plugins: List<Plugin>,
    private val backStack: BackStackFeature<Configuration>
) : Node<Nothing>(
    buildParams = buildParams,
    viewFactory = null,
    plugins = plugins
), Portal {

    override fun showDefault(): Single<Rib> =
        attachWorkflow<Rib>{
            backStack.push(Content.Default)
        }.rx2()

    override fun showInPortal(ancestryInfo: AncestryInfo): Single<Rib> =
        attachWorkflow<Rib> {
            backStack.push(Content.Portal(ancestryInfo.routingChain))
        }.rx2()
}
