package com.badoo.ribs.samples.comms_nodes.rib.greeting

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.samples.comms_nodes.R

interface GreetingView : RibView {

    interface Factory : ViewFactoryBuilder<Dependency, GreetingView>

    interface Dependency {
        val presenter: GreetingPresenter
    }

    sealed class Event {
        object ChangeLanguageClicked : Event()
    }

    data class ViewModel(
        val greeting: Text
    )

    fun accept(vm: ViewModel)
}

class GreetingViewImpl private constructor(
    override val androidView: ViewGroup,
    private val presenter: GreetingPresenter
) : AndroidRibView(),
    GreetingView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_greeting
    ) : GreetingView.Factory {
        override fun invoke(deps: GreetingView.Dependency): ViewFactory<GreetingView> = object : ViewFactory<GreetingView> {
            override fun invoke(context: ViewFactory.Context) = GreetingViewImpl(
                context.inflate(layoutRes),
                presenter = deps.presenter
            )
        }
    }

    private val greetingTextView: TextView = androidView.findViewById(R.id.greeting_text)
    private val changeLanguageButton: Button = androidView.findViewById(R.id.change_language_button)

    init {
        changeLanguageButton.setOnClickListener { presenter.onEvent(GreetingView.Event.ChangeLanguageClicked) }
    }

    override fun accept(vm: GreetingView.ViewModel) {
        greetingTextView.text = vm.greeting.resolve(context)
    }
}
