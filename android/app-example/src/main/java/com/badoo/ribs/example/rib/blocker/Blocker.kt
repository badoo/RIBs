package com.badoo.ribs.example.rib.blocker

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.CanProvideRibCustomisation
import com.badoo.ribs.core.directory.RibCustomisation
import com.badoo.ribs.core.directory.inflateOnDemand
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.R
import io.reactivex.functions.Consumer

interface Blocker : Rib {

    interface Dependency : CanProvideRibCustomisation {
        fun blockerOutput(): Consumer<Output>
    }

    sealed class Output {
        object SomeEvent : Output()
    }

    class Customisation(
        val viewFactory: ViewFactory<BlockerView> = inflateOnDemand(
            R.layout.rib_blocker
        )
    ) : RibCustomisation
}
