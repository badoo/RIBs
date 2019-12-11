package com.badoo.ribs.example.rib.main_hello_world

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldView.Event
import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldView.ViewModel
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreen
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface MainHelloWorldView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    data class ViewModel(
        val text: String
    )

    interface Factory : ViewFactory<Nothing?, MainHelloWorldView>
}

class MainHelloWorldViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : MainHelloWorldView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_main_hello_world
    ) : MainHelloWorldView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> MainHelloWorldView = {
            MainHelloWorldViewImpl(
                inflate(it, layoutRes)
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

    override fun getParentViewForChild(child: Rib): ViewGroup? =
        when (child) {
            is PortalSubScreen -> smallContainer
            else -> null
        }
}
