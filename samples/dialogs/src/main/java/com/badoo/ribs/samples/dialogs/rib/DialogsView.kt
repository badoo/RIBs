package com.badoo.ribs.samples.dialogs.rib

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.dialogs.R
import com.badoo.ribs.samples.dialogs.rib.DialogsView.Event
import com.badoo.ribs.samples.dialogs.rib.DialogsView.Event.*
import com.badoo.ribs.samples.dialogs.rib.DialogsView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface DialogsView : RibView, ObservableSource<Event>, Consumer<ViewModel> {

    sealed class Event {
        object ShowThemedDialogClicked : Event()
        object ShowSimpleDialogClicked : Event()
        object ShowLazyDialogClicked : Event()
        object ShowRibDialogClicked : Event()
    }

    data class ViewModel(
            val text: String
    )

    interface Factory : ViewFactory<Nothing?, DialogsView>
}

class DialogsViewImpl private constructor(
        override val androidView: ViewGroup,
        private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(), DialogsView, ObservableSource<Event> by events, Consumer<ViewModel> {

    class Factory(@LayoutRes private val layoutRes: Int = R.layout.rib_dialogs) : DialogsView.Factory {

        override fun invoke(deps: Nothing?): (RibView) -> DialogsView = {
            DialogsViewImpl(it.inflate(layoutRes))
        }
    }

    private val simpleDialogButton: Button = androidView.findViewById(R.id.dialogs_rib_simple_dialog_button)
    private val themedDialogButton: Button = androidView.findViewById(R.id.dialogs_rib_themed_dialog_button)
    private val ribDialogButton: Button = androidView.findViewById(R.id.dialogs_rib_rib_dialog_button)
    private val lazyDialogButton: Button = androidView.findViewById(R.id.dialogs_rib_lazy_dialog_button)
    private val text: TextView = androidView.findViewById(R.id.dialogs_rib_debug)


    init {
        themedDialogButton.setOnClickListener { events.accept(ShowThemedDialogClicked) }
        simpleDialogButton.setOnClickListener { events.accept(ShowSimpleDialogClicked) }
        lazyDialogButton.setOnClickListener { events.accept(ShowLazyDialogClicked) }
        ribDialogButton.setOnClickListener { events.accept(ShowRibDialogClicked) }
    }

    override fun accept(vm: ViewModel) {
        text.text = vm.text
    }
}