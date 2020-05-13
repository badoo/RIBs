package com.badoo.ribs.sandbox.rib.foo_bar

import androidx.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarView.Event
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarView.Event.CheckPermissionsButtonClicked
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarView.Event.RequestPermissionsButtonClicked
import com.badoo.ribs.sandbox.rib.foo_bar.FooBarView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface FooBarView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object CheckPermissionsButtonClicked : Event()
        object RequestPermissionsButtonClicked : Event()
    }

    data class ViewModel(
        val text: String
    )

    interface Factory : ViewFactory<Nothing?, FooBarView>
}


class FooBarViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : FooBarView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_foobar
    ) : FooBarView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> FooBarView = {
            FooBarViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private val text: TextView = androidView.findViewById(R.id.foobar_debug)
    private val checkButton: Button = androidView.findViewById(R.id.foobar_button_check_permissions)
    private val requestButton: Button = androidView.findViewById(R.id.foobar_button_request_permissions)

    init {
        checkButton.setOnClickListener { events.accept(CheckPermissionsButtonClicked)}
        requestButton.setOnClickListener { events.accept(RequestPermissionsButtonClicked)}
    }

    override fun accept(vm: ViewModel) {
        text.text = vm.text
    }
}
