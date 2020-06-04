package com.badoo.ribs.tutorials.tutorial3.rib.hello_world

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams

class HelloWorldInteractor(
    buildParams: BuildParams<Nothing?>
) : Interactor<HelloWorld, HelloWorldView>(
    buildParams = buildParams,
    disposables = null
) {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
    }
}
