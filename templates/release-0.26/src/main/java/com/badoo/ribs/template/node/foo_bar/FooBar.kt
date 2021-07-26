package com.badoo.ribs.template.node.foo_bar

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.template.node.foo_bar.FooBar.Input
import com.badoo.ribs.template.node.foo_bar.FooBar.Output
import com.badoo.ribs.template.node.foo_bar.routing.FooBarRouter

interface FooBar : Rib, Connectable<Input, Output> {

    interface Dependency : RootDependency, RibDependency

    interface RootDependency

    interface RibDependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory(),
        val transitionHandler: TransitionHandler<FooBarRouter.Configuration>? = null
    ) : RibCustomisation
}
