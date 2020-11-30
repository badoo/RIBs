package com.badoo.ribs.sandbox.documentation.hello_world

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams


class HelloWorldBuilder(
    private val dependency: HelloWorld.Dependency
) : SimpleBuilder<HelloWorld>() {

    override fun build(buildParams: BuildParams<Nothing?>): HelloWorld {
        val presenter = HelloWorldPresenter(greeting = "Hello ${dependency.name}!") // <--

        return HelloWorldNode(
            viewFactory = HelloWorldViewImpl.Factory().invoke(deps = null),
            plugins = listOf(presenter) // <--
        )
    }
}

