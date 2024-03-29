package com.badoo.ribs.samples.android.dialogs.rib.dummy

import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl

interface DummyPresenter {

    fun handle(event: DummyView.Event)
}

internal class DummyPresenterImpl(
    private val ribAware: RibAware<Dummy> = RibAwareImpl()
) : DummyPresenter, RibAware<Dummy> by ribAware {

    override fun handle(event: DummyView.Event) {
        when (event) {
            is DummyView.Event.ButtonClicked -> rib.output.emit(Dummy.Output.SomeEvent)
        }
    }
}
