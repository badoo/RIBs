package com.badoo.ribs.example.rib.dialog_example

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.Event
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.Event.ShowRibDialogClicked
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface DialogExampleView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ShowSimpleDialogClicked : Event()
        object ShowLazyDialog : Event()
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
) : DialogExampleView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_dialog_example
    ) : DialogExampleView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> DialogExampleView = {
            DialogExampleViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private val showSimpleDialog: Button = androidView.findViewById(R.id.show_simple_dialog)
    private val showRibDialog: Button = androidView.findViewById(R.id.show_rib_dialog)
    private val showLazyDialog: Button = androidView.findViewById(R.id.show_lazy_dialog)
    private val text: TextView = androidView.findViewById(R.id.dialogs_example_debug)

    init {
        showSimpleDialog.setOnClickListener { events.accept(ShowSimpleDialogClicked) }
        showLazyDialog.setOnClickListener { events.accept(Event.ShowLazyDialog) }
        showRibDialog.setOnClickListener { events.accept(ShowRibDialogClicked) }
    }

    override fun accept(vm: ViewModel) {
        text.text = vm.text
    }
}
