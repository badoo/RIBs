package com.badoo.ribs.tutorials.tutorial2.rib.hello_world

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorldRouter.Configuration
import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorldRouter.Configuration.Content
import com.badoo.ribs.core.BuildContext

class HelloWorldInteractor(
    buildContext: BuildContext.Resolved<Nothing?>,
    router: Router<Configuration, Nothing, Content, Nothing, HelloWorldView>
) : Interactor<Configuration, Content, Nothing, HelloWorldView>(
    buildContext = buildContext,
    router = router,
    disposables = null
)
