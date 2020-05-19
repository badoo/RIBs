package com.badoo.ribs.tutorials.tutorial2.rib.hello_world

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.builder.BuildParams

class HelloWorldInteractor(
    buildParams: BuildParams<Nothing?>
) : Interactor<HelloWorld, HelloWorldView>(
    buildParams = buildParams,
    disposables = null
)
