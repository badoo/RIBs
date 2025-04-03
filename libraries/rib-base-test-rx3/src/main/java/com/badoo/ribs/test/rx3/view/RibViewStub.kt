package com.badoo.ribs.test.rx3.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.test.assertEquals
import com.jakewharton.rxrelay3.PublishRelay
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Consumer
import org.mockito.Mockito.mock

/* Sync with other versions. */
@SuppressLint("CheckResult")
open class RibViewStub<ViewModel : Any, ViewEvent : Any>(
    val viewModelRelay: Relay<ViewModel> = PublishRelay.create(),
    val viewEventRelay: Relay<ViewEvent> = PublishRelay.create()
) : AndroidRibView(),
    Consumer<ViewModel> by viewModelRelay,
    ObservableSource<ViewEvent> by viewEventRelay {

    init {
        viewModelRelay.subscribe { lastViewModel = it }
    }

    var lastViewModel: ViewModel? = null
        private set

    override val androidView: ViewGroup = mock(ViewGroup::class.java)

    @SuppressLint("MissingSuperCall")
    override fun saveInstanceState(bundle: Bundle) {
        // no-op (view is mocked)
    }

    @SuppressLint("MissingSuperCall")
    override fun restoreInstanceState(bundle: Bundle?) {
        // no-op (view is mocked)
    }

    fun assertViewModel(viewModel: ViewModel) {
        assertEquals(viewModel, lastViewModel)
    }

}
