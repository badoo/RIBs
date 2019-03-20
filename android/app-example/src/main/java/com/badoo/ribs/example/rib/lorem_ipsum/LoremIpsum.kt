package com.badoo.ribs.example.rib.lorem_ipsum

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.inflateOnDemand
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.R
import io.reactivex.functions.Consumer

interface LoremIpsum : Rib {

    interface Dependency : Rib.Dependency {
        fun loremIpsumOutput(): Consumer<Output>
    }

    sealed class Output {
        object SomeEvent : Output()
    }

    class Customisation(
        val viewFactory: ViewFactory<LoremIpsumView> = inflateOnDemand(
            R.layout.rib_lorem_ipsum
        )
    )
}
