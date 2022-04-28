package com.badoo.ribs.template.node.foo_bar

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.template.node.foo_bar.FooBar.Input
import com.badoo.ribs.template.node.foo_bar.FooBar.Output
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter
import io.reactivex.Single

interface FooBar : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory(),
        val transitionHandler: TransitionHandler<FooBarRouter.Configuration>? = null
    ) : RibCustomisation

    // Workflow
    // todo: rename rather than delete, and add more
    // todo: expose all meaningful operations
    fun businessLogicOperation(): Single<FooBar>

    // todo: expose all possible children (even permanent parts), or remove if there's none
    // fun attachChild1(): Single<Child>
}
