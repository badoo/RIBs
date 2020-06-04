package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container

import com.badoo.ribs.clienthelper.interactor.BackStackInteractor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerRouter.Configuration

class GreetingsContainerInteractor(
    buildParams: BuildParams<Nothing?>
) : BackStackInteractor<GreetingsContainer, Nothing, Configuration>(
    buildParams = buildParams,
    initialConfiguration = Configuration.Default
) {

}
