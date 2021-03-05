package com.badoo.ribs.tutorials.tutorial5.rib.hello_world

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.jakewharton.rxrelay2.PublishRelay
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.tutorials.tutorial5.R
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorldView.Event
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorldView.ViewModel
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.ViewFactory
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface HelloWorldView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object HelloButtonClicked : Event()
    }

    data class ViewModel(
        val titleText: Text,
        val welcomeText: Text,
        val buttonText: Text
    )

    interface Factory : ViewFactoryBuilder<Nothing?, HelloWorldView>
}

class HelloWorldViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    HelloWorldView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_hello_world
    ) : HelloWorldView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<HelloWorldView> = ViewFactory {
            HelloWorldViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val title: TextView = androidView.findViewById(R.id.hello_world_title)
    private val welcome: TextView = androidView.findViewById(R.id.hello_world_welcome)
    private val helloButton: Button = androidView.findViewById(R.id.hello_world_hello_button)
    private val moreOptionsButton: Button = androidView.findViewById(R.id.hello_world_more_options)

    init {
        helloButton.setOnClickListener { events.accept(Event.HelloButtonClicked) }
        // TODO set click listener on moreOptionsButton to emit event
    }

    override fun accept(vm: ViewModel) {
        title.text = vm.titleText.resolve(androidView.context)
        welcome.text = vm.welcomeText.resolve(androidView.context)
        helloButton.text = vm.buttonText.resolve(androidView.context)
    }
}
