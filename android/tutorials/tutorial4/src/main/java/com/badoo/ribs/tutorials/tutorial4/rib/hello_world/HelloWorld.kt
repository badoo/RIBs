package com.badoo.ribs.tutorials.tutorial4.rib.hello_world

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.inflateOnDemand
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.tutorials.tutorial4.R
import io.reactivex.functions.Consumer

interface HelloWorld : Rib {

    interface Dependency : Rib.Dependency {
        fun helloWorldOutput(): Consumer<Output>
    }

    sealed class Output {
        object HelloThere : Output()
    }

    class Customisation(
        val viewFactory: ViewFactory<HelloWorldView> = inflateOnDemand(
            R.layout.rib_hello_world
        )
    )
}
