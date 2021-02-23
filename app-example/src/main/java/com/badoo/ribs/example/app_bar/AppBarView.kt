package com.badoo.ribs.example.app_bar

import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import coil.api.load
import coil.transform.CircleCropTransformation
import com.badoo.mvicore.ModelWatcher
import com.badoo.mvicore.modelWatcher
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.NodeViewFactory
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
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

    interface Factory : ViewFactory<Nothing?, AppBarView>
}


class AppBarViewImpl private constructor(
    override val androidView: ViewGroup,
    val lifecycle: Lifecycle,
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

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                // do something
            }
        })
    }

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_app_bar
    ) : AppBarView.Factory {
        override fun invoke(deps: Nothing?): NodeViewFactory<AppBarView> =
            object : NodeViewFactory<AppBarView> {
                override fun invoke(p1: RibView, p2: Lifecycle): AppBarView =
                    AppBarViewImpl(
                        androidView = p1.inflate(layoutRes),
                        lifecycle = p2
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
