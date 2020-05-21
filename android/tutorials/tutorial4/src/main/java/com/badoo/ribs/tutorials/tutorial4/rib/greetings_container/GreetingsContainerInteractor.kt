package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container

import com.badoo.ribs.core.BackStackInteractor
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainerRouter.Configuration
import io.reactivex.functions.Consumer
import com.badoo.ribs.core.builder.BuildParams

class GreetingsContainerInteractor(
    buildParams: BuildParams<Nothing?>
) : BackStackInteractor<GreetingsContainer, Nothing, Configuration>(
    buildParams = buildParams,
    initialConfiguration = Configuration.HelloWorld
) {

}
