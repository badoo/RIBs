package com.badoo.ribs.samples.android.dialogs.rib.dummy

import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.android.dialogs.R
import com.badoo.ribs.samples.android.dialogs.rib.dummy.DummyView.Dependency
import com.badoo.ribs.samples.android.dialogs.rib.dummy.DummyView.Event

interface DummyView : RibView {

    sealed class Event {
        object ButtonClicked : Event()
    }

    interface Dependency {
        val presenter: DummyPresenter
    }

    interface Factory : ViewFactoryBuilder<Dependency, DummyView>
}

class DummyViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: DummyPresenter
) : AndroidRibView(), DummyView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_dummy
    ) : DummyView.Factory {
        override fun invoke(deps: Dependency): ViewFactory<DummyView> = ViewFactory {
            DummyViewImpl(it.inflate(layoutRes), deps.presenter)
        }
    }

    private val button: Button = androidView.findViewById(R.id.dummy_rib_button)

    init {
        button.setOnClickListener { presenter.handle(Event.ButtonClicked) }
    }
}
