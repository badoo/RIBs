package com.badoo.ribs.samples.comms_nodes.rib.greeting

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewAware

interface GreetingPresenter {
    fun onEvent(event: GreetingView.Event)
}

internal class GreetingPresenterImpl(
    ribAware: RibAware<Node<GreetingView>> = RibAwareImpl()
) : GreetingPresenter,
    ViewAware<GreetingView>,
    RibAware<Node<GreetingView>> by ribAware {

    override fun onViewCreated(view: GreetingView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        view.onChange(GreetingView.ViewModel(Text.Plain("Hello")))
    }

    override fun onEvent(event: GreetingView.Event) {
        // TODO: open the next screen
    }
}