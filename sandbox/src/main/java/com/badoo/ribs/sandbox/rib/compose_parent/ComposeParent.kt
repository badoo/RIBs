package com.badoo.ribs.sandbox.rib.compose_parent

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParent.Input
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParent.Output
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter

interface ComposeParent : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: ComposeParentView.Factory = ComposeParentViewImpl.Factory(),
        val transitionHandler: TransitionHandler<ComposeParentRouter.Configuration>? = null
    ) : RibCustomisation
}
