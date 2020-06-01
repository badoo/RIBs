package com.badoo.ribs.core.routing.portal

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.configuration.feature.operation.push
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration
import com.badoo.ribs.core.routing.portal.PortalRouter.Configuration.Content
import io.reactivex.Single

class PortalNode internal constructor(
    buildParams: BuildParams<*>,
    plugins: List<Plugin>,
    private val backStack: BackStackFeature<Configuration>
) : Node<Nothing>(
    buildParams = buildParams,
    viewFactory = null,
    plugins = plugins
), Portal {

    override fun showDefault(): Single<Rib> =
        attachWorkflow {
            backStack.push(Content.Default)
        }

    override fun showInPortal(ancestryInfo: AncestryInfo): Single<Rib> =
        attachWorkflow {
            backStack.push(Content.Portal(ancestryInfo.configurationChain))
        }
}
