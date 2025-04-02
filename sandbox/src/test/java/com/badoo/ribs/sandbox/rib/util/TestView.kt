package com.badoo.ribs.sandbox.rib.util

import android.view.ViewGroup
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.jakewharton.rxrelay3.PublishRelay
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Consumer
import org.mockito.kotlin.mock

abstract class TestView<M : Any, E : Any>(
    override val androidView: ViewGroup = mock(),
    private val _viewModel: Relay<M> = PublishRelay.create(),
    private val _uiEvents: Relay<E> = PublishRelay.create()
) : AndroidRibView(),
    RibView,
    Consumer<M> by _viewModel, ObservableSource<E> by _uiEvents {

    val viewModel: ObservableSource<M>
        get() = _viewModel

    val uiEvents: Consumer<E>
        get() = _uiEvents

}
