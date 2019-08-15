package com.badoo.ribs.template.rib_with_view.foo_bar

import android.support.annotation.LayoutRes
import android.view.ViewGroup
import com.badoo.mvicore.modelWatcher
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.template.R
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarView.Event
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface FooBarView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val someField: Int = 0
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
        @LayoutRes private val layoutRes: Int = R.layout.rib_foo_bar
    ) : FooBarView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> FooBarView = {
            FooBarViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    // feel free to check https://badoo.github.io/MVICore/extras/modelwatcher/ for usage examples
    private val modelWatcher = modelWatcher<ViewModel> {
        // TODO
        // ViewModel::someField {
        //     androidView.setSomething(it)
        // }
    }

    override fun accept(vm: ViewModel) {
        modelWatcher.invoke(vm)
    }
}
