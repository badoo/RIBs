package com.badoo.ribs.tutorials.tutorial2.rib.hello_world

import com.badoo.ribs.core.Rib

interface HelloWorld : Rib<HelloWorldView> {

    interface Dependency

    class Customisation(
        val viewFactory: HelloWorldView.Factory = HelloWorldViewImpl.Factory()
    )
}
