package com.badoo.ribs.sandbox.rib.blocker

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
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
