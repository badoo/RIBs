package com.badoo.ribs.tutorials.tutorial2.rib.hello_world

import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams

class HelloWorldInteractor(
    buildParams: BuildParams<Nothing?>
) : Interactor<HelloWorld, HelloWorldView>(
    buildParams = buildParams
)
