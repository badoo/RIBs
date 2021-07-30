package com.badoo.ribs.samples.back_stack.rib.back_stack_example

import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.back_stack.R
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.BackStackExampleView.Event.Child
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.BackStackExampleView.Event.Content
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.BackStackExampleView.Event.Overlay

interface BackStackExampleView : RibView {

    fun setBackStack(backStack: String)

    sealed class Event {

        enum class Child {
            A, B, C, D, E, F
        }

        sealed class Content : Event() {
            object Pop : Content()
            data class Push(val child: Child) : Content()
            data class Replace(val child: Child) : Content()
            data class NewRoot(val child: Child) : Content()
            data class SingleTop(val child: Child) : Content()
        }

        sealed class Overlay : Event() {
            object Pop : Overlay()
            data class Push(val child: Child) : Overlay()
        }

    }

    interface Factory : ViewFactoryBuilder<Dependency, BackStackExampleView>

    interface Dependency {
        val presenter: BackStackExamplePresenter
    }

}


class BackStackExampleViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: BackStackExamplePresenter
) : AndroidRibView(),
    BackStackExampleView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_back_stack_example
    ) : BackStackExampleView.Factory {
        override fun invoke(deps: BackStackExampleView.Dependency): ViewFactory<BackStackExampleView> = ViewFactory {
            BackStackExampleViewImpl(
                androidView = it.inflate(layoutRes),
                presenter = deps.presenter
            )
        }
    }

    private val contentContainer: ViewGroup = androidView.findViewById(R.id.children_container)

    private val backStackTextView: TextView = androidView.findViewById(R.id.back_stack_text)

    private val contentRadioGroup: RadioGroup = androidView.findViewById(R.id.content_radio_group)
    private val aRadioButton: RadioButton = androidView.findViewById(R.id.a_radio_button)
    private val bRadioButton: RadioButton = androidView.findViewById(R.id.b_radio_button)
    private val cRadioButton: RadioButton = androidView.findViewById(R.id.c_radio_button)
    private val dRadioButton: RadioButton = androidView.findViewById(R.id.d_radio_button)
    private val pushContentButton: Button = androidView.findViewById(R.id.push_content_button)
    private val replaceContentButton: Button = androidView.findViewById(R.id.replace_content_button)
    private val popContentButton: Button = androidView.findViewById(R.id.pop_content_button)
    private val newRootContentButton: Button = androidView.findViewById(R.id.new_root_content_button)
    private val singleTopContentButton: Button = androidView.findViewById(R.id.single_top_content_button)

    private val overlayRadioGroup: RadioGroup = androidView.findViewById(R.id.overlay_radio_group)
    private val eRadioButton: RadioButton = androidView.findViewById(R.id.e_radio_button)
    private val fRadioButton: RadioButton = androidView.findViewById(R.id.f_radio_button)
    private val pushOverlayButton: Button = androidView.findViewById(R.id.push_overlay_button)
    private val popOverlayButton: Button = androidView.findViewById(R.id.pop_overlay_button)

    init {
        popContentButton.setOnClickListener { presenter.handle(Content.Pop) }
        pushContentButton.setOnClickListener { presenter.handle(Content.Push(contentSelectedChild)) }
        replaceContentButton.setOnClickListener { presenter.handle(Content.Replace(contentSelectedChild)) }
        newRootContentButton.setOnClickListener { presenter.handle(Content.NewRoot(contentSelectedChild)) }
        singleTopContentButton.setOnClickListener { presenter.handle(Content.SingleTop(contentSelectedChild)) }

        popOverlayButton.setOnClickListener { presenter.handle(Overlay.Pop) }
        pushOverlayButton.setOnClickListener { presenter.handle(Overlay.Push(overlaySelectedChild)) }
    }

    private val contentSelectedChild: Child
        get() = when (contentRadioGroup.checkedRadioButtonId) {
            aRadioButton.id -> Child.A
            bRadioButton.id -> Child.B
            cRadioButton.id -> Child.C
            dRadioButton.id -> Child.D
            else -> throw IllegalStateException("No content radio button selected")
        }

    private val overlaySelectedChild: Child
        get() = when (overlayRadioGroup.checkedRadioButtonId) {
            eRadioButton.id -> Child.E
            fRadioButton.id -> Child.F
            else -> throw IllegalStateException("No overlay radio button selected")
        }

    override fun setBackStack(backStack: String) {
        backStackTextView.text = androidView.resources.getString(R.string.back_stack_with_args, backStack)
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        contentContainer

}
