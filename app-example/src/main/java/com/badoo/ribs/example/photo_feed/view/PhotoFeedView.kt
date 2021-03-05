package com.badoo.ribs.example.photo_feed.view

import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.example.R
import com.badoo.ribs.example.photo_feed.Photo
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.Event
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.ViewModel
import com.badoo.ribs.example.photo_feed.view.PhotoListItem.PhotoItem
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PhotoFeedView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ScrolledToTheEnd : Event()
        object RetryInitialLoadingClicked : Event()
        object RetryNextPageLoadingClicked : Event()
        data class PhotoClicked(val photo: Photo) : Event()
    }

    sealed class ViewModel {

        data class Loaded(val photos: List<Photo>) : ViewModel()
        data class LoadingNext(val photos: List<Photo>) : ViewModel()
        data class LoadingNextError(val photos: List<Photo>) : ViewModel()
        object InitialLoading : ViewModel()
        object InitialLoadingError : ViewModel()

    }

    interface Factory : ViewFactoryBuilder<Nothing?, PhotoFeedView>
}


class PhotoFeedViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    PhotoFeedView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_photo_feed
    ) : PhotoFeedView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<PhotoFeedView> = ViewFactory {
            PhotoFeedViewImpl(
                it.inflate(layoutRes)
            )
        }
    }

    private val adapter = PhotosAdapter()
    private val photoList = findViewById<RecyclerView>(R.id.photoFeed_photosList)
    private val initialLoader = findViewById<ProgressBar>(R.id.photoFeed_initialLoader)
    private val initialLoadingError = findViewById<Group>(R.id.photoFeed_initialLoadingError)
    private val retryInitialLoading = findViewById<Button>(R.id.photoFeed_retryInitialLoading)

    init {
        photoList.adapter = adapter
        (photoList.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        retryInitialLoading.setOnClickListener {
            events.accept(Event.RetryInitialLoadingClicked)
        }
    }

    override fun accept(vm: ViewModel) {
        when (vm) {
            is ViewModel.Loaded -> showLoaded(vm)
            is ViewModel.InitialLoading -> showInitialLoading()
            is ViewModel.LoadingNext -> showNextPageLoading(vm)
            is ViewModel.LoadingNextError -> showNextPageLoadingError(vm)
            is ViewModel.InitialLoadingError -> showInitialLoadingError()
        }
    }

    private fun showNextPageLoadingError(vm: ViewModel.LoadingNextError) {
        showListItems(
            vm.photos.toItems() + listOf(
                PhotoListItem.NextPageLoadingErrorItem {
                    events.accept(Event.RetryNextPageLoadingClicked)
                }
            )
        )
    }

    private fun showNextPageLoading(vm: ViewModel.LoadingNext) {
        showListItems(
            vm.photos.toItems() + listOf(PhotoListItem.NextPageLoadingItem)
        )
    }

    private fun showLoaded(vm: ViewModel.Loaded) {
        showListItems(vm.photos.toItems()) {
            events.accept(Event.ScrolledToTheEnd)
        }
    }

    private fun showListItems(items: List<PhotoListItem>, onEndReached: (() -> Unit)? = null) {
        adapter.submitList(items)
        initialLoader.visibility = GONE
        initialLoadingError.visibility = GONE
        photoList.visibility = VISIBLE

        if (onEndReached != null) {
            photoList.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val endReached = !recyclerView.canScrollVertically(1)
                        if (endReached) {
                            onEndReached.invoke()
                        }
                    }
                }
            )
        } else {
            photoList.clearOnScrollListeners()
        }

    }

    private fun showInitialLoadingError() {
        initialLoader.visibility = GONE
        photoList.visibility = GONE
        initialLoadingError.visibility = VISIBLE
    }

    private fun showInitialLoading() {
        photoList.visibility = GONE
        initialLoadingError.visibility = GONE
        initialLoader.visibility = VISIBLE
    }

    private fun List<Photo>.toItems(): List<PhotoListItem> = map {
        PhotoItem(
            photo = it,
            onClicked = { events.accept(Event.PhotoClicked(it)) }
        )

    }

}
