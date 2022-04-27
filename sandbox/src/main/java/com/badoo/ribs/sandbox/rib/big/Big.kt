package com.badoo.ribs.sandbox.rib.big

import com.badoo.ribs.core.Rib
import com.badoo.ribs.portal.CanProvidePortal
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface Big : Rib {

    interface Dependency : CanProvidePortal

    class Customisation(
        val viewFactory: BigView.Factory = BigViewImpl.Factory()
    ) : NodeCustomisation
}
