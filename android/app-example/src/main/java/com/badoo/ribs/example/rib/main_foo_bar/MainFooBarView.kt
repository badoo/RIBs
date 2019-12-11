package com.badoo.ribs.example.rib.main_foo_bar

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.main_foo_bar.MainFooBarView.Event
import com.badoo.ribs.example.rib.main_foo_bar.MainFooBarView.Event.CheckPermissionsButtonClicked
import com.badoo.ribs.example.rib.main_foo_bar.MainFooBarView.Event.RequestPermissionsButtonClicked
import com.badoo.ribs.example.rib.main_foo_bar.MainFooBarView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface MainFooBarView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object CheckPermissionsButtonClicked : Event()
        object RequestPermissionsButtonClicked : Event()
    }

    data class ViewModel(
        val text: String
    )

    interface Factory : ViewFactory<Nothing?, MainFooBarView>
}


class MainFooBarViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : MainFooBarView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_main_foobar
    ) : MainFooBarView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> MainFooBarView = {
            MainFooBarViewImpl(
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
