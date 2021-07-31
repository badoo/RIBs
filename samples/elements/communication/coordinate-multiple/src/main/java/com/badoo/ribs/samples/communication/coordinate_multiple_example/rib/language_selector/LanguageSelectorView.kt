package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.language_selector

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.communication.coordinate_multiple_example.R
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.language_selector.Language.ENGLISH
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.language_selector.Language.FRENCH
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.language_selector.Language.GERMAN

interface LanguageSelectorView : RibView {

    interface Factory : ViewFactoryBuilder<Dependency, LanguageSelectorView>

    interface Dependency {
        val presenter: LanguageSelectorPresenter
    }

    data class ViewModel(
        val selectionIndex: Int
    )

    fun accept(vm: ViewModel)
}

class LanguageSelectorViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: LanguageSelectorPresenter
) : AndroidRibView(),
    LanguageSelectorView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_language_selector
    ) : LanguageSelectorView.Factory {
        override fun invoke(deps: LanguageSelectorView.Dependency): ViewFactory<LanguageSelectorView> =
            ViewFactory { context ->
                LanguageSelectorViewImpl(
                    context.inflate(layoutRes),
                    presenter = deps.presenter
                )
            }
    }

    private val confirmLanguageButton: Button = androidView.findViewById(R.id.confirm_language_button)
    private val radioGroup: RadioGroup = androidView.findViewById(R.id.languages_radio_group)

    init {
        createRadioButtons()
        confirmLanguageButton.setOnClickListener { presenter.onLanguageConfirmed(radioGroup.checkedIndex) }
    }

    private fun createRadioButtons() {
        Language.values().forEachIndexed { index, language ->
            RadioButton(context).apply {
                text = language.toDisplayText()
                id = View.generateViewId()
                radioGroup.addView(this)
                isChecked = index == 0
            }
        }
    }

    private val RadioGroup.checkedIndex: Int
        get() = indexOfChild(findViewById<RadioButton>(checkedRadioButtonId))

    override fun accept(vm: LanguageSelectorView.ViewModel) {
        val selectionId = radioGroup.getChildAt(vm.selectionIndex).id
        radioGroup.check(selectionId)
    }

    private fun Language.toDisplayText(): String =
        when (this) {
            ENGLISH -> "English"
            GERMAN -> "German"
            FRENCH -> "French"
        }
}
