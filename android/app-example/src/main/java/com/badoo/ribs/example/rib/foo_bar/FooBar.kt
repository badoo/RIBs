package com.badoo.ribs.example.rib.foo_bar

import com.badoo.ribs.android.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.CanProvideRibCustomisation
import com.badoo.ribs.core.directory.RibCustomisation
import com.badoo.ribs.core.directory.inflateOnDemand
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.R
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface FooBar : Rib {

    interface Dependency : CanProvidePermissionRequester, CanProvideRibCustomisation {
        fun foobarInput(): ObservableSource<Input>
        fun foobarOutput(): Consumer<Output>
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: ViewFactory<FooBarView> = inflateOnDemand(
            R.layout.rib_foobar
        )
    ) : RibCustomisation
}
