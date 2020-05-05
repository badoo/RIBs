package com.badoo.ribs.example.rib.big

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.RibCustomisation

interface Big : Rib {

    interface Dependency :
        CanProvidePortal

    class Customisation(
        val viewFactory: BigView.Factory = BigViewImpl.Factory()
    ) : RibCustomisation

    interface Workflow
}
