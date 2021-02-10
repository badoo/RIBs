package com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance

import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.retained_instance_store.R

interface RetainedInstanceView : RibView {

    interface Factory : ViewFactory<Dependency, RetainedInstanceView>

    interface Dependency {
        val presenter: RetainedInstancePresenter
    }
}

class RetainedInstanceViewImpl private constructor(
        override val androidView: ViewGroup,
        private val presenter: RetainedInstancePresenter
) : AndroidRibView(),
        RetainedInstanceView {

    class Factory(
            @LayoutRes private val layoutRes: Int = R.layout.rib_retained_instance
    ) : RetainedInstanceView.Factory {
        override fun invoke(deps: RetainedInstanceView.Dependency): (RibView) -> RetainedInstanceView = {
            RetainedInstanceViewImpl(
                    androidView = it.inflate(layoutRes),
                    presenter = deps.presenter
            )
        }
    }

    private val childrenContainer = androidView.findViewById<FrameLayout>(R.id.children_container)
    private val toggleChildButton = androidView.findViewById<Button>(R.id.toggle_child_button)

    init {
        toggleChildButton.setOnClickListener { presenter.toggleChildCounter() }
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
            childrenContainer
}