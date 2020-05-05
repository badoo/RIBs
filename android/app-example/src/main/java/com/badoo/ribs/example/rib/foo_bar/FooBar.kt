package com.badoo.ribs.example.rib.foo_bar

import com.badoo.ribs.android.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.RibCustomisation
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface FooBar : Rib {

    interface Dependency : CanProvidePermissionRequester {
        fun foobarInput(): ObservableSource<Input>
        fun foobarOutput(): Consumer<Output>
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: FooBarView.Factory = FooBarViewImpl.Factory()
    ) : RibCustomisation

    interface Workflow {

    }
}
