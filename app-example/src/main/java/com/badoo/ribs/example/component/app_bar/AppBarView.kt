package com.badoo.ribs.example.component.app_bar

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.R
import com.badoo.ribs.example.component.app_bar.AppBarView.Event
import com.badoo.ribs.example.component.app_bar.AppBarView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface AppBarView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, AppBarView>
}


class AppBarViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AppBarView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_app_bar
    ) : AppBarView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> AppBarView = {
            AppBarViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    override fun accept(vm: AppBarView.ViewModel) {
    }
}
