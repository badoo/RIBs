package com.badoo.ribs.example.rib.hello_world

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.TextView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.hello_world.HelloWorldView.Event
import com.badoo.ribs.example.rib.hello_world.HelloWorldView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface HelloWorldView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    data class ViewModel(
        val text: String
    )
}

class HelloWorldViewImpl  private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : HelloWorldView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_dialog_example
    ) : ViewFactory<HelloWorld.Dependency, HelloWorldView> {
        override fun invoke(deps: HelloWorld.Dependency): (ViewGroup) -> HelloWorldView = {
            HelloWorldViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private val text: TextView = androidView.findViewById(R.id.hello_debug)
    private val launchButton: TextView = androidView.findViewById(R.id.hello_button_launch)

    init {
        launchButton.setOnClickListener { events.accept(Event.ButtonClicked) }
    }

    override fun accept(vm: ViewModel) {
        text.text = vm.text
    }
}
