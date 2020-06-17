package com.badoo.ribs.sandbox.rib.compose_parent

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParent.Input
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParent.Output
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter
import io.reactivex.Single

interface ComposeParent : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: ComposeParentView.Factory = ComposeParentViewImpl.Factory(),
        val transitionHandler: TransitionHandler<ComposeParentRouter.Configuration>? = null
    ) : RibCustomisation

    // Workflow
    // todo: do not delete - rename, and add more
    // todo: expose all meaningful operations
    fun businessLogicOperation(): Single<ComposeParent>

    // todo: expose all possible children (even permanent parts), or remove if there's none
    // fun attachChild1(): Single<Child>
}
