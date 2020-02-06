package com.badoo.ribs.tutorials.tutorial2.rib.hello_world

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorldRouter.Configuration
import com.badoo.ribs.tutorials.tutorial2.rib.hello_world.HelloWorldRouter.Configuration.Content
import com.badoo.ribs.core.BuildParams

class HelloWorldInteractor(
    buildParams: BuildParams<Nothing?>,
    router: Router<Configuration, Nothing, Content, Nothing, HelloWorldView>
) : Interactor<Configuration, Content, Nothing, HelloWorldView>(
    buildParams = buildParams,
    router = router,
    disposables = null
)
