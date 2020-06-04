package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container

import com.badoo.ribs.clienthelper.interactor.BackStackInteractor
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainerRouter.Configuration

class GreetingsContainerInteractor(
    buildParams: BuildParams<Nothing?>
) : BackStackInteractor<GreetingsContainer, Nothing, Configuration>(
    buildParams = buildParams,
    initialConfiguration = Configuration.HelloWorld
) {

}
