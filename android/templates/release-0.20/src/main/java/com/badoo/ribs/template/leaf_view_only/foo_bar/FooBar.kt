package com.badoo.ribs.template.leaf_view_only.foo_bar

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.RibCustomisation
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.Consumer

interface FooBar : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory()
    ) : RibCustomisation
}
