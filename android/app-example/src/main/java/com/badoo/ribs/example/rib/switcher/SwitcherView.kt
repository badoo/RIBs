package com.badoo.ribs.example.rib.switcher

import android.view.ViewGroup
import android.widget.Button
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory2
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.switcher.SwitcherView.Event
import com.badoo.ribs.example.rib.switcher.SwitcherView.ViewModel
import com.badoo.ribs.example.util.CoffeeMachine
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface SwitcherView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ShowOverlayDialogClicked : Event()
        object ShowBlockerClicked: Event()
    }

    data class ViewModel(
        val i: Int = 0
    )
}


class SwitcherViewImpl private constructor(
    override val androidView: ViewGroup,
    private val coffeeMachine: CoffeeMachine,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : SwitcherView,
    ObservableSource<Event> by events {

    companion object Factory : ViewFactory2<Switcher.Dependency, SwitcherView> {
        override fun invoke(deps: Switcher.Dependency): (ViewGroup) -> SwitcherView = {
            SwitcherViewImpl(
                inflate(it, R.layout.rib_switcher),
                deps.coffeeMachine()
            )
        }
    }

    private val menuContainer: ViewGroup = androidView.findViewById(R.id.menu_container)
    private val contentContainer: ViewGroup = androidView.findViewById(R.id.content_container)
    private val blockerContainer: ViewGroup = androidView.findViewById(R.id.blocker_container)
    private val showOverlayDialog: Button = androidView.findViewById(R.id.show_overlay_dialog)
    private val showBlocker: Button = androidView.findViewById(R.id.show_blocker)
    private val makeCoffee: Button = androidView.findViewById(R.id.make_coffee)

    init {
        showOverlayDialog.setOnClickListener { events.accept(Event.ShowOverlayDialogClicked) }
        showBlocker.setOnClickListener { events.accept(Event.ShowBlockerClicked) }
        makeCoffee.setOnClickListener { coffeeMachine.makeCoffee(androidView.context) }
    }

    override fun accept(vm: ViewModel) {
    }

    override fun getParentViewForChild(child: Rib): ViewGroup? =
        when (child) {
            is Menu -> menuContainer
            is Blocker -> blockerContainer
            else -> contentContainer
        }
}
