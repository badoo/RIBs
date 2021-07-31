package com.badoo.ribs.example.photo_feed.mapper

import com.badoo.ribs.example.photo_feed.feature.PhotoFeedFeature.State
import com.badoo.ribs.example.photo_feed.view.PhotoFeedView.ViewModel

internal object StateToViewModel : (State) -> ViewModel {

    override fun invoke(state: State): ViewModel =
        when (state) {
            is State.InitialLoading -> ViewModel.InitialLoading
            is State.InitialLoadingError -> ViewModel.InitialLoadingError
            is State.Loaded -> ViewModel.Loaded(state.photos)
            is State.LoadingNext -> ViewModel.LoadingNext(state.photos)
            is State.LoadingNextError -> ViewModel.LoadingNextError(state.photos)
        }
}
