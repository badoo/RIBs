package com.badoo.ribs.example.rib.blocker

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.RibCustomisation
import io.reactivex.functions.Consumer

interface Blocker : Rib {

    interface Dependency {
        fun blockerOutput(): Consumer<Output>
    }

    sealed class Output {
        object SomeEvent : Output()
    }

    class Customisation(
        val viewFactory: BlockerView.Factory = BlockerViewImpl.Factory()
    ) : RibCustomisation
}
