package com.badoo.ribs.sandbox.rib.dialog_example

import androidx.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowLazyDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowRibDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowThemeDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface DialogExampleView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ShowThemeDialogClicked : Event()
        object ShowSimpleDialogClicked : Event()
        object ShowLazyDialogClicked : Event()
        object ShowRibDialogClicked : Event()
    }

    data class ViewModel(
        val text: String
    )

    interface Factory : ViewFactory<Nothing?, DialogExampleView>
}

class DialogExampleViewImpl  private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    DialogExampleView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_dialog_example
    ) : DialogExampleView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> DialogExampleView = {
            DialogExampleViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val showThemeDialog: Button = androidView.findViewById(R.id.show_theme_dialog)
    private val showSimpleDialog: Button = androidView.findViewById(R.id.show_simple_dialog)
    private val showRibDialog: Button = androidView.findViewById(R.id.show_rib_dialog)
    private val showLazyDialog: Button = androidView.findViewById(R.id.show_lazy_dialog)
    private val text: TextView = androidView.findViewById(R.id.dialogs_example_debug)

    init {
        showThemeDialog.setOnClickListener { events.accept(ShowThemeDialogClicked) }
        showSimpleDialog.setOnClickListener { events.accept(ShowSimpleDialogClicked) }
        showLazyDialog.setOnClickListener { events.accept(ShowLazyDialogClicked) }
        showRibDialog.setOnClickListener { events.accept(ShowRibDialogClicked) }
    }

    override fun accept(vm: ViewModel) {
        text.text = vm.text
    }
}
