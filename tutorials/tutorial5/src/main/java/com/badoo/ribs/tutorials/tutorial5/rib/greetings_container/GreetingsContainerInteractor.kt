package com.badoo.ribs.tutorials.tutorial5.rib.greetings_container

import com.badoo.ribs.core.BackStackInteractor
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.tutorials.tutorial5.rib.greetings_container.GreetingsContainer.Output.GreetingsSaid
import com.badoo.ribs.tutorials.tutorial5.rib.greetings_container.GreetingsContainerRouter.Configuration
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorld.Output.HelloThere
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelector
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.functions.Consumer

class GreetingsContainerInteractor(
    buildParams: BuildParams<Nothing?>,
    output: Consumer<GreetingsContainer.Output>
) : BackStackInteractor<GreetingsContainer, Nothing, Configuration>(
    buildParams = buildParams,
    initialConfiguration = Configuration.HelloWorld
) {
    internal val helloWorldInputSource: Relay<HelloWorld.Input> = PublishRelay.create()
    internal val helloWorldOutputConsumer: Consumer<HelloWorld.Output> = Consumer {
        when (it) {
            is HelloThere -> output.accept(GreetingsSaid(it.greeting))
        }
    }

    internal val optionsSelectorOutputConsumer: Consumer<OptionSelector.Output> = Consumer {
        // TODO
    }
}
