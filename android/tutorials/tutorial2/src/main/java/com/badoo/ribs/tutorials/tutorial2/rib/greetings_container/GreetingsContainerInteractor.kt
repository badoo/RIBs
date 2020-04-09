package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerRouter.Configuration
import io.reactivex.functions.Consumer

class GreetingsContainerInteractor(
    buildParams: BuildParams<Nothing?>,
    private val router: Router<Configuration, Nothing, Configuration, Nothing, Nothing>,
    output: Consumer<GreetingsContainer.Output>
) : Interactor<Nothing>(
    buildParams = buildParams,
    disposables = null
) {

}
