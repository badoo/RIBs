package com.badoo.ribs.example.component.app_bar

import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.R
import com.badoo.ribs.example.component.app_bar.AppBarView.Event
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

interface AppBarView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        object UserClicked : Event()
        object SearchClicked : Event()
    }

    interface Factory : ViewFactory<Nothing?, AppBarView>
}


class AppBarViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AppBarView,
    ObservableSource<Event> by events {

    private val startAction: ImageView = androidView.findViewById(R.id.toolbar_action_start)
    private val endAction: ImageView = androidView.findViewById(R.id.toolbar_action_end)

    init {
        startAction.setOnClickListener {
            events.accept(Event.UserClicked)
        }

        endAction.setOnClickListener {
            events.accept(Event.SearchClicked)
        }
    }

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_app_bar
    ) : AppBarView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> AppBarView = {
            AppBarViewImpl(
                inflate(it, layoutRes)
            )
        }
    }
}
