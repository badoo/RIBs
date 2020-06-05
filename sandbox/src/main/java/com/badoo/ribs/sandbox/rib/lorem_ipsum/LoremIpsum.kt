package com.badoo.ribs.sandbox.rib.lorem_ipsum

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsum.Input
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsum.Output

interface LoremIpsum : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        object SomeEvent : Output()
    }

    class Customisation(
        val viewFactory: LoremIpsumView.Factory = LoremIpsumViewImpl.Factory()
    ) : RibCustomisation
}
