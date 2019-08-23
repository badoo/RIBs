package com.badoo.ribs.example.rib.big

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.Consumer

interface Big : Rib {

    interface Dependency :
        CanProvideRibCustomisation,
        CanProvidePortal

    class Customisation(
        val viewFactory: BigView.Factory = BigViewImpl.Factory()
    ) : RibCustomisation

    interface Workflow
}
