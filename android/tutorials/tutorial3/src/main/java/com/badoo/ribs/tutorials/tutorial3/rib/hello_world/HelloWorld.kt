package com.badoo.ribs.tutorials.tutorial3.rib.hello_world

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflateOnDemand
import com.badoo.ribs.tutorials.tutorial3.R

interface HelloWorld : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: ViewFactory<HelloWorldView> = inflateOnDemand(
            R.layout.rib_hello_world
        )
    )
}
