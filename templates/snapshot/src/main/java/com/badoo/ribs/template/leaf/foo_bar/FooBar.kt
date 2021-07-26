package com.badoo.ribs.template.leaf.foo_bar

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.template.leaf.foo_bar.FooBar.Input
import com.badoo.ribs.template.leaf.foo_bar.FooBar.Output

interface FooBar : Rib, Connectable<Input, Output> {

    interface Dependency : RootDependency, RibDependency

    interface RootDependency

    interface RibDependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory()
    ) : RibCustomisation
}
