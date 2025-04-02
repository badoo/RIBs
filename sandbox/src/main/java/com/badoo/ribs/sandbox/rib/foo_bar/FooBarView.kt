package com.badoo.ribs.sandbox.rib.foo_bar

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarView.Event
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarView.Event.CheckPermissionsButtonClicked
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarView.Event.RequestPermissionsButtonClicked
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarView.ViewModel
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Consumer

interface FooBarView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        data object CheckPermissionsButtonClicked : Event()
        data object RequestPermissionsButtonClicked : Event()
    }

    data class ViewModel(
        val text: String
    )

    interface Factory : ViewFactoryBuilder<Nothing?, FooBarView>
}


class FooBarViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    FooBarView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_foobar
    ) : FooBarView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<FooBarView> = ViewFactory {
            FooBarViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val text: TextView = androidView.findViewById(R.id.foobar_debug)
    private val checkButton: Button = androidView.findViewById(R.id.foobar_button_check_permissions)
    private val requestButton: Button =
        androidView.findViewById(R.id.foobar_button_request_permissions)

    init {
        checkButton.setOnClickListener { events.accept(CheckPermissionsButtonClicked) }
        requestButton.setOnClickListener { events.accept(RequestPermissionsButtonClicked) }
    }

    override fun accept(vm: ViewModel) {
        text.text = vm.text
    }
}
