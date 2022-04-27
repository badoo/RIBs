package com.badoo.ribs.sandbox.rib.foo_bar

import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar.Input
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar.Output
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface FooBar : Rib, Connectable<Input, Output> {

    interface Dependency : CanProvidePermissionRequester

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory()
    ) : NodeCustomisation
}
