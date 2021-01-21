package com.samples.helloworld.hello_world.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.samples.helloworld.hello_world.HelloWorld
import com.samples.helloworld.hello_world.HelloWorldNode
import com.samples.helloworld.hello_world.HelloWorldPresenter
import com.samples.helloworld.hello_world.HelloWorldPresenterImpl
import com.samples.helloworld.hello_world.HelloWorldView
import com.samples.helloworld.hello_world.HelloWorldViewImpl

class HelloWorldBuilder(
    private val dependency: HelloWorld.Dependency
) : SimpleBuilder<HelloWorld>() {

    override fun build(buildParams: BuildParams<Nothing?>): HelloWorld {
        val presenter = HelloWorldPresenterImpl(greeting = "Hello ${dependency.name}!")
        val viewDependencies: HelloWorldView.Dependency = object : HelloWorldView.Dependency {
            override val presenter: HelloWorldPresenter = presenter
        }
        return HelloWorldNode(
            viewFactory = HelloWorldViewImpl.Factory().invoke(deps = viewDependencies),
            plugins = listOf(presenter)
        )
    }
}
