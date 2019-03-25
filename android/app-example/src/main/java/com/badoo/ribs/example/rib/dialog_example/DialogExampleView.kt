package com.badoo.ribs.example.rib.dialog_example

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import com.badoo.ribs.core.view.RibView
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
}

class DialogExampleViewImpl private constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, private val events: PublishRelay<Event>
) : ConstraintLayout(context, attrs, defStyle),
    DialogExampleView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
    ) : this(context, attrs, defStyle, PublishRelay.create<Event>())

    override val androidView = this
    private val showSimpleDialog: Button by lazy { findViewById<Button>(R.id.show_simple_dialog) }
    private val showRibDialog: Button by lazy { findViewById<Button>(R.id.show_rib_dialog) }
    private val showLazyDialog: Button by lazy { findViewById<Button>(R.id.show_lazy_dialog) }
    private val text: TextView by lazy { findViewById<TextView>(R.id.dialogs_example_debug) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        showSimpleDialog.setOnClickListener { events.accept(ShowSimpleDialogClicked) }
        showLazyDialog.setOnClickListener { events.accept(Event.ShowLazyDialog) }
        showRibDialog.setOnClickListener { events.accept(ShowRibDialogClicked) }
    }

    override fun accept(vm: ViewModel) {
        text.text = vm.text
    }
}
