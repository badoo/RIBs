package com.badoo.ribs.example.rib.hello_world

import com.badoo.ribs.android.CanProvideActivityStarter
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.CanProvideRibCustomisation
import com.badoo.ribs.customisation.RibCustomisation
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.functions.Consumer

interface HelloWorld : Rib<HelloWorldView> {

    interface Dependency :
        CanProvideActivityStarter,
        CanProvideRibCustomisation,
        CanProvidePortal {
        fun helloWorldInput(): ObservableSource<Input>
        fun helloWorldOutput(): Consumer<Output>
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: HelloWorldView.Factory = HelloWorldViewImpl.Factory()
    ) : RibCustomisation

    // Workflow
    fun somethingSomethingDarkSide(): Single<HelloWorld>
}
