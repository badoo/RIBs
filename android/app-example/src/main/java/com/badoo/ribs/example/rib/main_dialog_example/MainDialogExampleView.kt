package com.badoo.ribs.example.rib.main_dialog_example

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExampleView.Event
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExampleView.Event.ShowRibDialogClicked
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExampleView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExampleView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface MainDialogExampleView : RibView,
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

    interface Factory : ViewFactory<Nothing?, MainDialogExampleView>
}

class MainDialogExampleViewImpl  private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : MainDialogExampleView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_dialog_example
    ) : MainDialogExampleView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> MainDialogExampleView = {
            MainDialogExampleViewImpl(
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
