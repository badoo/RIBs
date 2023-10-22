package com.badoo.ribs.template.node.foo_bar.common.view

import com.badoo.ribs.template.node.foo_bar.common.view.FooBarView.Event
import com.badoo.ribs.template.node.foo_bar.common.view.FooBarView.ViewModel
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface FooBarView : ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )
}
