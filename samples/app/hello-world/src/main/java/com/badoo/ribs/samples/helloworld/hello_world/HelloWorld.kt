package com.badoo.ribs.samples.helloworld.hello_world

import com.badoo.ribs.core.Rib

interface HelloWorld : Rib {

    interface Dependency {
        val name: String
    }
}
