package com.badoo.ribs.template.node.foo_bar.v1

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.template.leaf_view_only.foo_bar.common.FooBar
import com.badoo.ribs.template.node.foo_bar.common.FooBar.Input
import com.badoo.ribs.template.node.foo_bar.common.FooBar.Output
import com.badoo.ribs.template.node.foo_bar.v1.routing.FooBarRouter
import io.reactivex.Single

interface FooBarRib : FooBar, Rib, Connectable<Input, Output> {

    class Customisation(
        val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory(),
        val transitionHandler: TransitionHandler<FooBarRouter.Configuration>? = null
    ) : RibCustomisation

    // Workflow
    // todo: rename rather than delete, and add more
    // todo: expose all meaningful operations
    fun businessLogicOperation(): Single<FooBarRib>

    // todo: expose all possible children (even permanent parts), or remove if there's none
    // fun attachChild1(): Single<Child>
}
