package com.badoo.ribs.example.rib.switcher

import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
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

    interface Factory : ViewFactory<Dependency, SwitcherView>

    interface Dependency {
        fun coffeeMachine(): CoffeeMachine
    }
}


class SwitcherViewImpl private constructor(
    override val androidView: ViewGroup,
    private val coffeeMachine: CoffeeMachine,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : SwitcherView,
    ObservableSource<Event> by events {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_switcher
    ) : SwitcherView.Factory {
        override fun invoke(deps: SwitcherView.Dependency): (ViewGroup) -> SwitcherView = {
            SwitcherViewImpl(
                inflate(it, layoutRes),
                deps.coffeeMachine()
            )
        }
    }

    private val menuContainer: ViewGroup = androidView.findViewById(R.id.menu_container)
    private val contentContainer: ViewGroup = androidView.findViewById(R.id.content_container)
//    private val blockerContainer: ViewGroup = androidView.findViewById(R.id.blocker_container)
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

    override fun getParentViewForChild(child: Node<*>): ViewGroup? =
        when (child.identifier) {
            is Menu -> menuContainer
//            is Blocker -> blockerContainer
            else -> contentContainer
        }
}
