package com.badoo.ribs.portal

import android.os.Parcelable
import com.badoo.ribs.core.BackStackInteractor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.source.backstack.operation.push
import com.badoo.ribs.core.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.portal.PortalRouter.Configuration
import com.badoo.ribs.portal.PortalRouter.Configuration.Content
import com.badoo.ribs.portal.PortalRouter.Configuration.Overlay

internal class PortalInteractor(
    buildParams: BuildParams<Nothing?>
) : BackStackInteractor<Portal, Nothing, Configuration>(
    buildParams = buildParams,
    initialConfiguration = Content.Default
), Portal.OtherSide {

    override fun showContent(remoteNode: Node<*>, remoteConfiguration: Parcelable) {
        backStack.push(Content.Portal(remoteNode.ancestryInfo.configurationChain + remoteConfiguration))
    }

    override fun showOverlay(remoteNode: Node<*>, remoteConfiguration: Parcelable) {
        backStack.pushOverlay(Overlay.Portal(remoteNode.ancestryInfo.configurationChain + remoteConfiguration))
    }
}
