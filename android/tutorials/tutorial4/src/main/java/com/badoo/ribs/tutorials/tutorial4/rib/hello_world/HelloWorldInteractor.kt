package com.badoo.ribs.tutorials.tutorial4.rib.hello_world

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Interactor

class HelloWorldInteractor(
    savedInstanceState: Bundle?
) : Interactor<HelloWorldView>(
    savedInstanceState = savedInstanceState,
    disposables = null
) {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
    }
}
