package com.badoo.ribs.samples.routing.transition_animations.rib.transition_animations_example

import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.routing.transition_animations.R

interface TransitionAnimationsExampleView : RibView {

    interface Factory : ViewFactoryBuilder<Dependency, TransitionAnimationsExampleView>

    interface Dependency {
        val presenter: TransitionAnimationsExamplePresenter
    }
}

class TransitionAnimationsExampleViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: TransitionAnimationsExamplePresenter
) : AndroidRibView(),
    TransitionAnimationsExampleView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_transition_animations
    ) : TransitionAnimationsExampleView.Factory {
        override fun invoke(deps: TransitionAnimationsExampleView.Dependency): ViewFactory<TransitionAnimationsExampleView> =
            ViewFactory {
                TransitionAnimationsExampleViewImpl(
                    androidView = it.inflate(layoutRes),
                    presenter = deps.presenter
                )
            }
    }

    private val nextButton: Button = androidView.findViewById(R.id.next_button)
    private val container: ViewGroup = androidView.findViewById(R.id.container)

    init {
        nextButton.setOnClickListener {
            presenter.goToNext()
        }
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup = container
}
