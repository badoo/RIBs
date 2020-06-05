package com.badoo.ribs.sandbox.rib.big

import com.badoo.ribs.core.Rib
import com.badoo.ribs.portal.CanProvidePortal
import com.badoo.ribs.core.customisation.RibCustomisation

interface Big : Rib {

    interface Dependency : com.badoo.ribs.portal.CanProvidePortal

    class Customisation(
        val viewFactory: BigView.Factory = BigViewImpl.Factory()
    ) : RibCustomisation
}
