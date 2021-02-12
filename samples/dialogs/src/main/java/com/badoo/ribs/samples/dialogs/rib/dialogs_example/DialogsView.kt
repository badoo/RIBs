package com.badoo.ribs.samples.dialogs.rib.dialogs_example

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.dialogs.R
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowLazyDialogClicked
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowRibDialogClicked
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowThemedDialogClicked

interface DialogsView : RibView {

    sealed class Event {
        object ShowThemedDialogClicked : Event()
        object ShowSimpleDialogClicked : Event()
        object ShowLazyDialogClicked : Event()
        object ShowRibDialogClicked : Event()
    }

    interface Dependency {
        val presenter: DialogsPresenter
    }

    interface Factory : ViewFactory<Dependency, DialogsView>

    fun displayText(headerText: String)
}

class DialogsViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: DialogsPresenter
) : AndroidRibView(), DialogsView {

    class Factory(@LayoutRes private val layoutRes: Int = R.layout.rib_dialogs) : DialogsView.Factory {

        override fun invoke(deps: DialogsView.Dependency): (RibView) -> DialogsView = {
            DialogsViewImpl(
                androidView = it.inflate(layoutRes),
                presenter = deps.presenter
            )
        }
    }

    private val simpleDialogButton: Button = androidView.findViewById(R.id.dialogs_rib_simple_dialog_button)
    private val themedDialogButton: Button = androidView.findViewById(R.id.dialogs_rib_themed_dialog_button)
    private val ribDialogButton: Button = androidView.findViewById(R.id.dialogs_rib_rib_dialog_button)
    private val lazyDialogButton: Button = androidView.findViewById(R.id.dialogs_rib_lazy_dialog_button)
    private val text: TextView = androidView.findViewById(R.id.dialogs_rib_debug)


    init {
        themedDialogButton.setOnClickListener { presenter.handleThemedDialog(ShowThemedDialogClicked) }
        simpleDialogButton.setOnClickListener { presenter.handleSimpleDialog(ShowSimpleDialogClicked) }
        lazyDialogButton.setOnClickListener { presenter.handleLazyDialog(ShowLazyDialogClicked) }
        ribDialogButton.setOnClickListener { presenter.handleRibDialog(ShowRibDialogClicked) }
    }

    override fun displayText(headerText: String) {
        text.text = headerText
    }
}
