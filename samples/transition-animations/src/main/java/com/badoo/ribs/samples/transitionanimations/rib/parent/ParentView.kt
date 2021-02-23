package com.badoo.ribs.samples.transitionanimations.rib.parent

import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.transitionanimations.R
import com.badoo.ribs.samples.transitionanimations.rib.parent.ParentView.Event

interface ParentView : RibView {

    sealed class Event {
        object PushNextChild : Event()
    }

    interface Factory : ViewFactory<Dependency, ParentView>

    interface Dependency {
        val presenter: ParentPresenter
    }
}

class ParentViewImpl private constructor(
        override val androidView: ViewGroup,
        private val presenter: ParentPresenter
) : AndroidRibView(),
        ParentView {

    class Factory(
            @LayoutRes private val layoutRes: Int = R.layout.rib_parent
    ) : ParentView.Factory {
        override fun invoke(deps: ParentView.Dependency): (RibView) -> ParentView = {
            ParentViewImpl(
                    androidView = it.inflate(layoutRes),
                    presenter = deps.presenter
            )
        }
    }

    private val nextButton: Button = androidView.findViewById(R.id.next_button)
    private val container: ViewGroup = androidView.findViewById(R.id.container)

    init {
        nextButton.setOnClickListener {
            presenter.handle(Event.PushNextChild)
        }
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup = container
}