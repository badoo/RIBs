package com.badoo.ribs.example.rib.hello_world

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.TextView
import com.badoo.mvicore.modelWatcher
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

    interface Factory : ViewFactory<Nothing?, HelloWorldView>
}

class HelloWorldViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : HelloWorldView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_hello_world
    ) : HelloWorldView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> HelloWorldView = {
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

    private val modelWatcher = modelWatcher<ViewModel> {
        ViewModel::text {
            text.text = it
        }
    }

    override fun accept(vm: ViewModel) {
        modelWatcher.invoke(vm)
    }
}
