package com.badoo.ribs.tutorials.tutorial4.rib.hello_world

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflateOnDemand
import com.badoo.ribs.tutorials.tutorial4.R
import com.badoo.ribs.tutorials.tutorial4.util.Lexem
import com.badoo.ribs.tutorials.tutorial4.util.User
import io.reactivex.functions.Consumer

interface HelloWorld : Rib {

    interface Dependency {
        fun user(): User
        fun config(): Config
        fun helloWorldOutput(): Consumer<Output>
    }

    sealed class Output {
        object HelloThere : Output()
    }

    data class Config(
        val welcomeMessage: Lexem
    )

    class Customisation(
        val viewFactory: ViewFactory<HelloWorldView> = inflateOnDemand(
            R.layout.rib_hello_world
        )
    )
}
