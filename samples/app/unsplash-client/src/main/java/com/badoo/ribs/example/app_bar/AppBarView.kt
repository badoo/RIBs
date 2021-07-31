package com.badoo.ribs.example.app_bar

import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import coil.load
import coil.transform.CircleCropTransformation
import com.badoo.mvicore.ModelWatcher
import com.badoo.mvicore.modelWatcher
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.example.R
import com.badoo.ribs.example.app_bar.AppBarView.Event
import com.badoo.ribs.example.app_bar.AppBarView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface AppBarView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object UserClicked : Event()
        object SearchClicked : Event()
    }

    data class ViewModel(
        val profileImageUrl: String
    )

    interface Factory : ViewFactoryBuilder<Nothing?, AppBarView>
}


class AppBarViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    AppBarView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

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
        override fun invoke(deps: Nothing?): ViewFactory<AppBarView> = ViewFactory {
            AppBarViewImpl(
                androidView = it.inflate(layoutRes)
            )
        }
    }

    private val modelWatcher: ModelWatcher<ViewModel> =
        modelWatcher {
            watch(ViewModel::profileImageUrl) {
                startAction.load(it) {
                    placeholder(R.drawable.ic_profile_placeholder)
                    transformations(CircleCropTransformation())
                }
            }
        }

    override fun accept(vm: ViewModel) {
        modelWatcher(vm)
    }
}
