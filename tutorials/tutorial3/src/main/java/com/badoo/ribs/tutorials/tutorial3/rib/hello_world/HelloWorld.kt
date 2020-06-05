package com.badoo.ribs.tutorials.tutorial3.rib.hello_world

import com.badoo.ribs.core.Rib

interface HelloWorld : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: HelloWorldView.Factory = HelloWorldViewImpl.Factory()
    )
}
