package com.badoo.ribs.sandbox.rib.hello_world

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorldView.Event
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorldView.ViewModel
import com.badoo.ribs.sandbox.rib.small.SmallNode
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

    private val text: TextView = androidView.findViewById(R.id.hello_debug)
    private val launchButton: TextView = androidView.findViewById(R.id.hello_button_launch)
    private val smallContainer: ViewGroup = androidView.findViewById(R.id.small_container)

    init {
        launchButton.setOnClickListener { events.accept(Event.ButtonClicked) }
    }

    override fun accept(vm: ViewModel) {
        text.text = vm.text
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        when (subtreeOf) {
            is SmallNode -> smallContainer
            else -> super.getParentViewForSubtree(subtreeOf)
        }
}
