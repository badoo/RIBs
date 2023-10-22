package com.badoo.ribs.template.leaf_view_only.foo_bar.v1

import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.template.leaf_view_only.foo_bar.common.FooBar
import com.badoo.ribs.template.leaf_view_only.foo_bar.common.FooBar.Input
import com.badoo.ribs.template.leaf_view_only.foo_bar.common.FooBar.Output

interface FooBarRib : Rib, Connectable<Input, Output>, FooBar {

    class Customisation(
        val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory()
    ) : RibCustomisation
}
