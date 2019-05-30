package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainer.Output
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainerRouter.Configuration
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorld
import io.reactivex.functions.Consumer

class GreetingsContainerInteractor(
    router: Router<Configuration, GreetingsContainerView>,
    output: Consumer<GreetingsContainer.Output>
) : Interactor<Configuration, GreetingsContainerView>(
    router = router,
    disposables = null
) {

    internal val helloWorldOutputConsumer: Consumer<HelloWorld.Output> = Consumer {
        when (it) {
            HelloWorld.Output.HelloThere -> output.accept(Output.GreetingsSaid("Hello there :)"))
        }
    }
}
