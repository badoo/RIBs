package com.badoo.ribs.example.rib.lorem_ipsum

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.CanProvideRibCustomisation
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.core.customisation.inflateOnDemand
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.R
import io.reactivex.functions.Consumer

interface LoremIpsum : Rib {

    interface Dependency : CanProvideRibCustomisation {
        fun loremIpsumOutput(): Consumer<Output>
    }

    sealed class Output {
        object SomeEvent : Output()
    }

    class Customisation(
        val viewFactory: ViewFactory<LoremIpsumView> = inflateOnDemand(
            R.layout.rib_lorem_ipsum
        )
    ) : RibCustomisation
}
