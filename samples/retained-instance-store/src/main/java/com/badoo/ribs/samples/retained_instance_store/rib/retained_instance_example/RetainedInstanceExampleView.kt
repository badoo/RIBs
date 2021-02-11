package com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example

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

interface RetainedInstanceExampleView : RibView {

    interface Factory : ViewFactory<Dependency, RetainedInstanceExampleView>

    interface Dependency {
        val presenter: RetainedInstanceExamplePresenter
    }
}

class RetainedInstanceExampleViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: RetainedInstanceExamplePresenter
) : AndroidRibView(),
    RetainedInstanceExampleView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_retained_instance
    ) : RetainedInstanceExampleView.Factory {
        override fun invoke(deps: RetainedInstanceExampleView.Dependency): (RibView) -> RetainedInstanceExampleView = {
            RetainedInstanceExampleViewImpl(
                androidView = it.inflate(layoutRes),
                presenter = deps.presenter
            )
        }
    }

    private val childrenContainer = androidView.findViewById<FrameLayout>(R.id.children_container)
    private val toggleChildButton = androidView.findViewById<Button>(R.id.toggle_child_button)

    init {
        toggleChildButton.setOnClickListener { presenter.toggleChildCounterConfig() }
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        childrenContainer
}