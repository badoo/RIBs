package com.badoo.ribs.sandbox.rib.compose_leaf

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeaf.Input
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeaf.Output

interface ComposeLeaf : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: ComposeLeafView.Factory = ComposeLeafViewImpl.Factory()
    ) : RibCustomisation
}
