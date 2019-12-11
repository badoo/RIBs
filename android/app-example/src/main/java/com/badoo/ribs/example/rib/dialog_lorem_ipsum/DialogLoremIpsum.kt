package com.badoo.ribs.example.rib.dialog_lorem_ipsum

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation
import io.reactivex.functions.Consumer

interface DialogLoremIpsum : Rib {

    interface Dependency : CanProvideRibCustomisation {
        fun loremIpsumOutput(): Consumer<Output>
    }

    sealed class Output {
        object SomeEvent : Output()
    }

    class Customisation(
        val viewFactory: DialogLoremIpsumView.Factory = DialogLoremIpsumViewImpl.Factory()
    ) : RibCustomisation
}
