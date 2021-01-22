package com.badoo.ribs.samples.back_stack.parent

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
import com.google.android.material.snackbar.Snackbar
import com.badoo.ribs.samples.back_stack.R
import com.badoo.ribs.samples.back_stack.parent.ParentView.Event.RoutingContent
import com.badoo.ribs.samples.back_stack.parent.ParentView.Event.RoutingContent.Content
import com.badoo.ribs.samples.back_stack.parent.ParentView.Event.RoutingOverlay
import com.badoo.ribs.samples.back_stack.parent.ParentView.Event.RoutingOverlay.Overlay

interface ParentView : RibView {

    fun setBackStack(backStack: String)

    sealed class Event {

        sealed class RoutingContent : Event() {
            enum class Content {
                A, B, C, D
            }

            object Pop : RoutingContent()

            data class Push(val content: Content) : RoutingContent()
            data class Replace(val content: Content) : RoutingContent()
            data class NewRoot(val content: Content) : RoutingContent()
            data class SingleTop(val content: Content) : RoutingContent()
        }

        sealed class RoutingOverlay : Event() {
            enum class Overlay {
                E, F
            }

            object Pop : RoutingOverlay()
            data class Push(val overlay: Overlay) : RoutingOverlay()
        }

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

    private val contentContainer: ViewGroup = androidView.findViewById(R.id.content_container)

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
        popContentButton.setOnClickListener { onEventClicked(RoutingContent.Pop) }
        pushContentButton.setOnClickListener { getContentSelectedRadioButton { onEventClicked(RoutingContent.Push(it)) } }
        replaceContentButton.setOnClickListener { getContentSelectedRadioButton { onEventClicked(RoutingContent.Replace(it)) } }
        newRootContentButton.setOnClickListener { getContentSelectedRadioButton { onEventClicked(RoutingContent.NewRoot(it)) } }
        singleTopContentButton.setOnClickListener { getContentSelectedRadioButton { onEventClicked(RoutingContent.SingleTop(it)) } }

        popOverlayButton.setOnClickListener { onEventClicked(RoutingOverlay.Pop) }
        pushOverlayButton.setOnClickListener { getOverlaySelectedRadioButton { onEventClicked(RoutingOverlay.Push(it)) } }
    }

    private fun getContentSelectedRadioButton(onClick: (Content) -> Unit) {
        when (contentRadioGroup.checkedRadioButtonId) {
            aRadioButton.id -> onClick(Content.A)
            bRadioButton.id -> onClick(Content.B)
            cRadioButton.id -> onClick(Content.C)
            dRadioButton.id -> onClick(Content.D)
            else -> Snackbar.make(androidView, context.getString(R.string.please_select_a_content), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getOverlaySelectedRadioButton(onClick: (Overlay) -> Unit) {
        when (overlayRadioGroup.checkedRadioButtonId) {
            eRadioButton.id -> onClick(Overlay.E)
            fRadioButton.id -> onClick(Overlay.F)
            else -> Snackbar.make(androidView, context.getString(R.string.please_select_an_overlay), Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun setBackStack(backStack: String) {
        backStackTextView.text = androidView.resources.getString(R.string.back_stack_with_args, backStack)
    }

    private fun onEventClicked(event: ParentView.Event) {
        presenter.onEventClicked(event)
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup = contentContainer

}
