package com.badoo.ribs.sandbox.rib.dialog_example

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowLazyDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowRibDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowThemedDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.Up
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.ViewModel
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Consumer

interface DialogExampleView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        data object ShowThemedDialogClicked : Event()
        data object ShowSimpleDialogClicked : Event()
        data object ShowLazyDialogClicked : Event()
        data object ShowRibDialogClicked : Event()
        data object Up : Event()
    }

    data class ViewModel(
        val text: String
    )

    interface Factory : ViewFactoryBuilder<Nothing?, DialogExampleView>
}

class DialogExampleViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    DialogExampleView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_dialog_example
    ) : DialogExampleView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<DialogExampleView> = ViewFactory {
            DialogExampleViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val showSimpleDialog: Button = androidView.findViewById(R.id.show_simple_dialog)
    private val showThemedDialog: Button = androidView.findViewById(R.id.show_themed_dialog)
    private val showRibDialog: Button = androidView.findViewById(R.id.show_rib_dialog)
    private val showLazyDialog: Button = androidView.findViewById(R.id.show_lazy_dialog)
    private val upNavigation: Button = androidView.findViewById(R.id.up_navigation)
    private val text: TextView = androidView.findViewById(R.id.dialogs_example_debug)

    init {
        showThemedDialog.setOnClickListener { events.accept(ShowThemedDialogClicked) }
        showSimpleDialog.setOnClickListener { events.accept(ShowSimpleDialogClicked) }
        showLazyDialog.setOnClickListener { events.accept(ShowLazyDialogClicked) }
        showRibDialog.setOnClickListener { events.accept(ShowRibDialogClicked) }
        upNavigation.setOnClickListener { events.accept(Up) }
    }

    override fun accept(vm: ViewModel) {
        text.text = vm.text
    }
}
