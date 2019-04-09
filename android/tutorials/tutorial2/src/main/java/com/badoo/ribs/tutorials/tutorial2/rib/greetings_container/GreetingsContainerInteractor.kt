package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerRouter.Configuration
import io.reactivex.functions.Consumer

class GreetingsContainerInteractor(
    router: Router<Configuration, GreetingsContainerView>,
    output: Consumer<GreetingsContainer.Output>
) : Interactor<Configuration, GreetingsContainerView>(
    router = router,
    disposables = null
)
