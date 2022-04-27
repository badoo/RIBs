package com.badoo.ribs.sandbox.rib.compose_leaf

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeaf.Input
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeaf.Output
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface ComposeLeaf : Rib, Connectable<Input, Output> {

    interface Dependency

    data class Params(
        val i: Int = 1
    )

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: ComposeLeafView.Factory = ComposeLeafViewImpl.Factory()
    ) : NodeCustomisation
}
