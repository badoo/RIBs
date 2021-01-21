package com.samples.helloworld.hello_world

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware

interface HelloWorldPresenter {

    fun onShowCountClicked()
}

internal class HelloWorldPresenterImpl(
    private val greeting: String
) : HelloWorldPresenter, ViewAware<HelloWorldView> {

    private var view: HelloWorldView? = null
    private var count: Int = 0

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        view.setText(greeting)
        viewLifecycle.subscribe(
            onCreate = { this@HelloWorldPresenterImpl.view = view },
            onDestroy = { this@HelloWorldPresenterImpl.view = null }
        )
    }

    override fun onShowCountClicked() {
        view?.showCount(++count)
    }
}
