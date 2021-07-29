package com.badoo.ribs.samples.dialogs.rib.dialogs_example

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.dialogs.R

interface DialogsExampleView : RibView {

    interface Dependency {
        val presenter: DialogsExamplePresenter
    }

    interface Factory : ViewFactoryBuilder<Dependency, DialogsExampleView>

    fun displayText(headerText: String)
}

class DialogsExampleViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: DialogsExamplePresenter
) : AndroidRibView(), DialogsExampleView {

    class Factory(@LayoutRes private val layoutRes: Int = R.layout.rib_dialogs) : DialogsExampleView.Factory {

        override fun invoke(deps: DialogsExampleView.Dependency): ViewFactory<DialogsExampleView> = ViewFactory {
            DialogsExampleViewImpl(
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
        themedDialogButton.setOnClickListener { presenter.handleThemedDialog() }
        simpleDialogButton.setOnClickListener { presenter.handleSimpleDialog() }
        lazyDialogButton.setOnClickListener { presenter.handleLazyDialog() }
        ribDialogButton.setOnClickListener { presenter.handleRibDialog() }
    }

    override fun displayText(headerText: String) {
        text.text = headerText
    }
}
