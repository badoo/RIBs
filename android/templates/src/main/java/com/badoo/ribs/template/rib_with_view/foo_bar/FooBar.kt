package com.badoo.ribs.template.rib_with_view.foo_bar

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface FooBar : Rib {

    interface Dependency : CanProvideRibCustomisation {
        fun fooBarInput(): ObservableSource<Input>
        fun fooBarOutput(): Consumer<Output>
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: ViewFactory<Dependency, FooBarView> = FooBarViewImpl.Factory()
    ) : RibCustomisation
}
