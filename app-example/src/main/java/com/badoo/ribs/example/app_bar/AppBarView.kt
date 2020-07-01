package com.badoo.ribs.example.app_bar

import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.badoo.mvicore.ModelWatcher
import com.badoo.mvicore.modelWatcher
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.R
import com.badoo.ribs.example.app_bar.AppBarView.Event
import com.badoo.ribs.example.app_bar.AppBarView.ViewModel
import com.badoo.ribs.example.image.ImageDownloader
import com.badoo.ribs.example.view.TargettableImageView
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
        val profileImageUrl: String,
        @DrawableRes val placeholderRes: Int
    )

    interface Factory : ViewFactory<ImageDownloader, AppBarView>
}


class AppBarViewImpl private constructor(
    override val androidView: ViewGroup,
    private val imageDownloader: ImageDownloader,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AppBarView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    private val startAction: TargettableImageView = androidView.findViewById(R.id.toolbar_action_start)
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
        override fun invoke(imageDownloader: ImageDownloader): (ViewGroup) -> AppBarView = {
            AppBarViewImpl(
                androidView = inflate(it, layoutRes),
                imageDownloader = imageDownloader
            )
        }
    }

    private val modelWatcher: ModelWatcher<ViewModel> =
        modelWatcher {
            watch({ it }, ViewModel::profileImageUrl or ViewModel::placeholderRes) {
                imageDownloader.download(it.profileImageUrl, it.placeholderRes, startAction)
            }
        }

    override fun accept(vm: ViewModel) {
        modelWatcher(vm)
    }
}
