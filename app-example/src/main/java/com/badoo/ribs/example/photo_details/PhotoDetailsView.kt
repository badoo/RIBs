package com.badoo.ribs.example.photo_details

import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.Group
import coil.load
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.example.R
import com.badoo.ribs.example.network.model.Photo
import com.badoo.ribs.example.photo_details.PhotoDetailsView.Event
import com.badoo.ribs.example.photo_details.PhotoDetailsView.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PhotoDetailsView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object LikeClicked : Event()
    }

    sealed class ViewModel {
        object Loading : ViewModel()
        data class Loaded(val detail: Photo) : ViewModel()
    }

    interface Factory : ViewFactoryBuilder<Nothing?, PhotoDetailsView>
}


class PhotoDetailsViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    PhotoDetailsView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_photo_details
    ) : PhotoDetailsView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<PhotoDetailsView> = ViewFactory {
            PhotoDetailsViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val loader = findViewById<ProgressBar>(R.id.photoDetail_loader)
    private val photo = findViewById<ImageView>(R.id.photoDetail_photo)
    private val userName = findViewById<TextView>(R.id.photoDetail_userName)
    private val like = findViewById<FloatingActionButton>(R.id.photoDetail_like)
    private val contentGroup = findViewById<Group>(R.id.photoDetail_content)


    override fun accept(vm: ViewModel) {
        when (vm) {
            is ViewModel.Loading -> showLoading()
            is ViewModel.Loaded -> showPhoto(vm.detail)
        }
    }

    private fun showPhoto(detail: Photo) {
        contentGroup.visibility = VISIBLE
        loader.visibility = GONE
        photo.load(detail.urls.small)
        userName.text = detail.user.username
        like.setImageResource(if (detail.likedByUser) R.drawable.ic_like else R.drawable.ic_like_empty)
        like.setOnClickListener {
            events.accept(Event.LikeClicked)
        }
    }

    private fun showLoading() {
        loader.visibility = VISIBLE
        contentGroup.visibility = GONE
    }
}
