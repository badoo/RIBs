package com.badoo.ribs.sandbox.rib.switcher

import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.menu.MenuNode
import com.badoo.ribs.sandbox.rib.switcher.SwitcherView.Event
import com.badoo.ribs.sandbox.rib.switcher.SwitcherView.ViewModel
import com.badoo.ribs.sandbox.util.CoffeeMachine
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface SwitcherView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ShowOverlayDialogClicked : Event()
        object ShowBlockerClicked : Event()
    }

    data class ViewModel(
        val uiFrozen: Boolean = false
    )

    interface Factory : ViewFactoryBuilder<Dependency, SwitcherView>

    interface Dependency {
        fun coffeeMachine(): CoffeeMachine
    }
}


class SwitcherViewImpl private constructor(
    override val androidView: ViewGroup,
    private val coffeeMachine: CoffeeMachine,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    SwitcherView,
    ObservableSource<Event> by events {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_switcher
    ) : SwitcherView.Factory {
        override fun invoke(deps: SwitcherView.Dependency): ViewFactory<SwitcherView> =
            ViewFactory {
                SwitcherViewImpl(
                    it.parent.inflate(layoutRes),
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
        Log.d("SwitcherViewImpl", "UI frozen: ${vm.uiFrozen}")
    }

    override fun getParentViewForSubtree(subtreeOf: Node<*>): ViewGroup =
        when (subtreeOf) {
            is MenuNode -> menuContainer
            else -> contentContainer
        }
}
