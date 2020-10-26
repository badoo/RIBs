package com.badoo.ribs.sandbox.documentation.hello_world

import com.badoo.ribs.core.Rib

interface HelloWorld : Rib {

    interface Dependency {
        val name: String
    }
}
