package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerRouter.Configuration
import io.reactivex.functions.Consumer
import com.badoo.ribs.core.BuildContext

class GreetingsContainerInteractor(
    buildContext: BuildContext.Resolved<Nothing?>,
    router: Router<Configuration, Nothing, Configuration, Nothing, Nothing>,
    output: Consumer<GreetingsContainer.Output>
) : Interactor<Configuration, Configuration, Nothing, Nothing>(
    buildContext = buildContext,
    router = router,
    disposables = null
) {

}
