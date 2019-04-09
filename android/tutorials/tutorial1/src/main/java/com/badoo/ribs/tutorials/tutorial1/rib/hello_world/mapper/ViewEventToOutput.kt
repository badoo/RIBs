package com.badoo.ribs.tutorials.tutorial1.rib.hello_world.mapper

import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorld.*
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldView

object ViewEventToOutput : (HelloWorldView.Event) -> HelloWorld.Output {
    override fun invoke(event: HelloWorldView.Event): HelloWorld.Output =
        when (event) {
            HelloWorldView.Event.ButtonClicked -> Output.HelloThere
        }
}
