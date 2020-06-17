package com.badoo.ribs.sandbox.rib.compose_parent

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParentView.Event
import com.badoo.ribs.sandbox.rib.compose_parent.ComposeParentView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface ComposeParentView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, ComposeParentView>
}


class ComposeParentViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeParentView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_compose_parent
    ) : ComposeParentView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> ComposeParentView = {
            ComposeParentViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    override fun accept(vm: ComposeParentView.ViewModel) {
    }
}
