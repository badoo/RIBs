package com.badoo.ribs.test.rx2.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.test.assertEquals
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
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

    fun assertViewModel(viewModel: ViewModel) {
        assertEquals(viewModel, lastViewModel)
    }

}
