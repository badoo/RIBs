package com.badoo.ribs.example.photo_feed.mapper

import com.badoo.ribs.example.photo_feed.feature.PhotoFeedFeature.State
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.ViewModel
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.ViewModel.InitialLoading
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.ViewModel.InitialLoadingError
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.ViewModel.Loaded
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.ViewModel.LoadingNext
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.ViewModel.LoadingNextError

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel =
        when {
            state.hasError && state.pageNumber == State.firstPageNumber -> InitialLoadingError
            state.isLoading && state.pageNumber == State.firstPageNumber -> InitialLoading
            state.isLoading && state.pageNumber > State.firstPageNumber -> LoadingNext(
                state.photos
            )
            state.hasError && state.pageNumber > State.firstPageNumber -> LoadingNextError(
                state.photos
            )
            !state.isLoading && !state.hasError -> Loaded(state.photos)
            else -> Loaded(state.photos)
        }
}
