package com.badoo.ribs.template.leaf_view_only.foo_bar

import com.badoo.ribs.clienthelper.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.RibCustomisation
import com.badoo.ribs.template.leaf_view_only.foo_bar.FooBar.Input
import com.badoo.ribs.template.leaf_view_only.foo_bar.FooBar.Output

interface FooBar : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory()
    ) : RibCustomisation
}
