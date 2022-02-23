package com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example

import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.retained_instance_store.R
import com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example.RetainedInstanceExampleView.*

interface RetainedInstanceExampleView : RibView {

    interface Factory : ViewFactoryBuilder<Dependency, RetainedInstanceExampleView>

    interface Dependency {
        val presenter: RetainedInstanceExamplePresenter
    }

    sealed class ButtonState(val resId: Int) {
        object CreateCounter : ButtonState(R.string.create_button_text)
        object DestroyCounter : ButtonState(R.string.destroy_button_text)
    }

    fun updateButtonState(state: ButtonState)
}

class RetainedInstanceExampleViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: RetainedInstanceExamplePresenter
) : AndroidRibView(),
    RetainedInstanceExampleView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_retained_instance
    ) : RetainedInstanceExampleView.Factory {
        override fun invoke(deps: Dependency): ViewFactory<RetainedInstanceExampleView> =
            ViewFactory {
                RetainedInstanceExampleViewImpl(
                    androidView = it.inflate(layoutRes),
                    presenter = deps.presenter
                )
            }
    }

    private val childrenContainer = androidView.findViewById<FrameLayout>(R.id.children_container)
    private val toggleChildButton = androidView.findViewById<Button>(R.id.toggle_child_button)

    init {
        toggleChildButton.setOnClickListener { presenter.toggleConfig() }
    }

    override fun updateButtonState(state: ButtonState) {
        toggleChildButton.text = getResourceString(state.resId)
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        childrenContainer

    private fun getResourceString(@StringRes stringId: Int) =
        androidView.resources.getText(stringId)
}