package com.badoo.ribs.sandbox.documentation.hello_world

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.plugin.ViewAware

internal class HelloWorldPresenter(
    private val greeting: String
) : ViewAware<HelloWorldView> {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        view.setText(greeting)
    }
}
