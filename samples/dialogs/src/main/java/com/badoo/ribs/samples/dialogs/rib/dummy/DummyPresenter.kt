package com.badoo.ribs.samples.dialogs.rib.dummy

import com.badoo.ribs.core.plugin.RibAware

interface DummyPresenter {

    fun handle(event: DummyView.Event)
}

internal class DummyPresenterImpl(
    private val ribAware: RibAware<Dummy>
) : DummyPresenter, RibAware<Dummy> by ribAware {

    override fun handle(event: DummyView.Event) {
        when (event) {
            is DummyView.Event.ButtonClicked -> rib.output.accept(Dummy.Output.SomeEvent)
        }
    }
}
