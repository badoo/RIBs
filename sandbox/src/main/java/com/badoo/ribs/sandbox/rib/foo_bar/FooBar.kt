package com.badoo.ribs.sandbox.rib.foo_bar

import com.badoo.ribs.android.CanProvidePermissionRequester
import com.badoo.ribs.clienthelper.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.RibCustomisation
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar.Input
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar.Output

interface FooBar : Rib, Connectable<Input, Output> {

    interface Dependency : CanProvidePermissionRequester

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory()
    ) : RibCustomisation
}
