package com.badoo.ribs.template.rib_with_view.foo_bar

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.template.R
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarView.Event
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface FooBarView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    interface Factory : ViewFactory<Dependency, FooBarView>

    interface Dependency

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )
}


class FooBarViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : FooBarView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_foo_bar
    ) : ViewFactory<FooBar.Dependency, FooBarView> {
        override fun invoke(deps: FooBar.Dependency): (ViewGroup) -> FooBarView = {
            FooBarViewImpl(
                com.badoo.ribs.customisation.inflate(it, layoutRes)
            )
        }
    }

    override fun accept(vm: FooBarView.ViewModel) {
    }
}
