package com.badoo.ribs.example.rib.dialog_example

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.inflateOnDemand
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.R
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface DialogExample : Rib {

    interface Dependency : Rib.Dependency {
//        fun dialogExampleInput(): ObservableSource<Input>
//        fun dialogExampleOutput(): Consumer<Output>
    }

//    sealed class Input

//    sealed class Output

    class Customisation(
        val viewFactory: ViewFactory<DialogExampleView> = inflateOnDemand(
            R.layout.rib_dialog_example
        )
    )
}
