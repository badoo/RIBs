package com.badoo.ribs.sandbox.rib.compose_leaf

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafView.Event
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface ComposeLeafView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, ComposeLeafView>
}


class ComposeLeafViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeLeafView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_compose_leaf
    ) : ComposeLeafView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> ComposeLeafView = {
            ComposeLeafViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    override fun accept(vm: ComposeLeafView.ViewModel) {
    }
}
