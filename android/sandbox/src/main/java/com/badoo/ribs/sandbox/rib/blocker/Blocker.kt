package com.badoo.ribs.sandbox.rib.blocker

import com.badoo.ribs.clienthelper.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.RibCustomisation
import com.badoo.ribs.sandbox.rib.blocker.Blocker.Input
import com.badoo.ribs.sandbox.rib.blocker.Blocker.Output

interface Blocker : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        object SomeEvent : Output()
    }

    class Customisation(
        val viewFactory: BlockerView.Factory = BlockerViewImpl.Factory()
    ) : RibCustomisation
}
