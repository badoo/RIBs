package com.badoo.ribs.sandbox.rib.small

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.portal.CanProvidePortal
import com.badoo.ribs.sandbox.rib.big.Big

interface Small : Rib {

    interface Dependency : CanProvidePortal

    interface ExtraDependencies : CanProvidePortal {
        fun customisation(): Small.Customisation
        fun buildParams(): BuildParams<Nothing?>
    }

    class Customisation(
        val viewFactory: SmallView.Factory = SmallViewImpl.Factory()
    ) : RibCustomisation

    // Workflow
}
