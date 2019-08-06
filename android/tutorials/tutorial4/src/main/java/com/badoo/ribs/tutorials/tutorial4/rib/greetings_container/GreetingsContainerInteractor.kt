package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainerRouter.Configuration
import io.reactivex.functions.Consumer
import android.os.Bundle

class GreetingsContainerInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Configuration, Nothing, Nothing>,
    output: Consumer<GreetingsContainer.Output>
) : Interactor<Configuration, Configuration, Nothing, Nothing>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {

}
