package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainerRouter.Configuration
import com.badoo.ribs.tutorials.tutorial4.util.BackStackInteractor

class GreetingsContainerInteractor(
    buildParams: BuildParams<Nothing?>
) : BackStackInteractor<GreetingsContainer, Nothing, Configuration>(
    buildParams = buildParams,
    initialConfiguration = Configuration.HelloWorld
) {

}
