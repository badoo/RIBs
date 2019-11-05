package com.badoo.ribs.tutorials.tutorial5.rib.option_selector

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.tutorials.tutorial5.R
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelectorView.Event
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelectorView.ViewModel
import com.badoo.ribs.tutorials.tutorial5.util.Lexem
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer


interface OptionSelectorView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        data class ConfirmButtonClicked(val selectionIndex: Int) : Event()
    }

    data class ViewModel(
        val options: List<Lexem>
    )

    interface Factory : ViewFactory<Nothing?, OptionSelectorView>
}


class OptionSelectorViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : OptionSelectorView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_option_selector
    ) : OptionSelectorView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> OptionSelectorView = {
            OptionSelectorViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private val radioGroup: RadioGroup = androidView.findViewById(R.id.more_options_radio_group)
    private val confirmButton: Button = androidView.findViewById(R.id.more_options_confirm_selection)

    init {
        // TODO set click listener on confirmButton that will emit ConfirmButtonClicked
        //  hint: use radioGroup.checkedIndex to set the value of ConfirmButtonClicked.selectionIndex
        confirmButton.setOnClickListener { events.accept(
            Event.ConfirmButtonClicked(radioGroup.checkedIndex))
        }
    }

    private val RadioGroup.checkedIndex: Int
        get() {
            val checkedRadio = findViewById<RadioButton>(checkedRadioButtonId)
            return indexOfChild(checkedRadio)
        }

    override fun accept(vm: ViewModel) {
        createRadios(vm.options)
    }

    fun createRadios(options: List<Lexem>) {
        radioGroup.removeAllViews()

        options.forEachIndexed() { idx, lexem ->
            RadioButton(androidView.context).apply {
                text = lexem.resolve(context)
                id = View.generateViewId()
                radioGroup.addView(this)
                isChecked = idx == 0
            }
        }
    }
}
