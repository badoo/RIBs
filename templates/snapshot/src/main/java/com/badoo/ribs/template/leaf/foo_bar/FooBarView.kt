package com.badoo.ribs.template.leaf.foo_bar

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.template.R
import com.badoo.ribs.template.leaf.foo_bar.FooBarView.Event
import com.badoo.ribs.template.leaf.foo_bar.FooBarView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface FooBarView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )

    fun interface Factory : ViewFactoryBuilder<Nothing?, FooBarView>
}


class FooBarViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    FooBarView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_foo_bar
    ) : FooBarView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<FooBarView> = ViewFactory {
            FooBarViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    override fun accept(vm: FooBarView.ViewModel) {
    }
}
