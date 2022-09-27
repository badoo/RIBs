package com.badoo.ribs.test.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.minimal.reactive.Emitter
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.test.assertEquals
import org.mockito.Mockito.mock

/* Sync with other versions. */
open class RibViewStub<ViewModel : Any, ViewEvent : Any>(
    val viewModelRelay: Relay<ViewModel> = Relay(),
    val viewEventRelay: Relay<ViewEvent> = Relay()
) : AndroidRibView(),
    Emitter<ViewModel> by viewModelRelay,
    Source<ViewEvent> by viewEventRelay {

    init {
        viewModelRelay.observe { lastViewModel = it }
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
