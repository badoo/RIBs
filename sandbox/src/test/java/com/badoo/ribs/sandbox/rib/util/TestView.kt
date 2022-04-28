package com.badoo.ribs.sandbox.rib.util

import android.view.ViewGroup
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import org.mockito.kotlin.mock
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

abstract class TestView<M, E>(
    override val androidView: ViewGroup = mock(),
    private val _viewModel: Relay<M> = PublishRelay.create<M>(),
    private val _uiEvents: Relay<E> = PublishRelay.create<E>()
) : AndroidRibView(),
    RibView,
    Consumer<M> by _viewModel, ObservableSource<E> by _uiEvents {

    val viewModel: ObservableSource<M>
        get() = _viewModel

    val uiEvents: Consumer<E>
        get() = _uiEvents

}
