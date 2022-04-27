package com.badoo.ribs.sandbox.rib.small

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.portal.CanProvidePortal
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface Small : Rib {

    interface Dependency : CanProvidePortal

    interface ExtraDependencies : CanProvidePortal {
        fun customisation(): Small.Customisation
        fun buildParams(): BuildParams<Nothing?>
    }

    class Customisation(
        val viewFactory: SmallView.Factory = SmallViewImpl.Factory()
    ) : NodeCustomisation

    // Workflow
}
