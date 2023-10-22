package com.badoo.ribs.template.leaf_view_only.foo_bar.common

import com.badoo.ribs.template.leaf_view_only.foo_bar.common.FooBarView.Event
import com.badoo.ribs.template.leaf_view_only.foo_bar.common.FooBarView.ViewModel
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface FooBarView : ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )
}