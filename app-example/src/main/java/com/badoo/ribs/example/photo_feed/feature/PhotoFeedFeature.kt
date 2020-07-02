package com.badoo.ribs.example.photo_feed.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.example.extensions.toObservable
import com.badoo.ribs.example.photo_feed.Photo
import com.badoo.ribs.example.photo_feed.PhotoFeedDataSource
import com.badoo.ribs.example.photo_feed.feature.PhotoFeedFeature.Effect
import com.badoo.ribs.example.photo_feed.feature.PhotoFeedFeature.News
import com.badoo.ribs.example.photo_feed.feature.PhotoFeedFeature.State
import com.badoo.ribs.example.photo_feed.feature.PhotoFeedFeature.Wish
import io.reactivex.Observable

internal class PhotoFeedFeature(
    dataSource: PhotoFeedDataSource
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State.Loaded(emptyList(), State.firstPageNumber),
    bootstrapper = BootStrapperImpl(),
    actor = ActorImpl(dataSource),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    sealed class State(
        open val pageNumber: Int = firstPageNumber,
        open val photos: List<Photo> = emptyList()
    ) {
        object InitialLoading : State()
        object InitialLoadingError : State()
        data class Loaded(
            override val photos: List<Photo>,
            override val pageNumber: Int
        ) : State(pageNumber, photos)

        data class LoadingNext(
            override val photos: List<Photo>,
            override val pageNumber: Int
        ) : State(pageNumber, photos)

        data class LoadingNextError(
            override val photos: List<Photo>,
            override val pageNumber: Int
        ) : State(pageNumber, photos)

        companion object {
            const val firstPageNumber = 1
        }
    }

    sealed class Wish {
        object LoadNextPage : Wish()
        object Refresh : Wish()
    }

    sealed class Effect {
        object LoadingStarted : Effect()
        data class PageLoaded(val photos: List<Photo>, val pageNumber: Int) : Effect()
        object LoadingFailed : Effect()
        object PhotosCleared : Effect()
    }

    sealed class News

    class BootStrapperImpl : Bootstrapper<Wish> {
        override fun invoke(): Observable<Wish> = Wish.LoadNextPage.toObservable()
    }

    class ActorImpl(
        private val dataSource: PhotoFeedDataSource
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> =
            when (wish) {
                is Wish.LoadNextPage ->
                    when (state) {
                        is State.InitialLoading, is State.LoadingNext -> Observable.empty()
                        else -> loadPage(state.pageNumber)
                    }
                is Wish.Refresh -> loadPage(0).startWith(Effect.PhotosCleared)
            }

        private fun loadPage(pageNumber: Int): Observable<Effect> {
            return dataSource.loadPhotos(pageNumber)
                .map<Effect> { photos ->
                    Effect.PageLoaded(photos, pageNumber.inc())
                }
                .onErrorReturnItem(Effect.LoadingFailed)
                .toObservable()
                .startWith(Effect.LoadingStarted)
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State =
            when (effect) {
                is Effect.PageLoaded -> State.Loaded(
                    state.photos + effect.photos, effect.pageNumber
                )
                is Effect.LoadingStarted -> when {
                    state is State.InitialLoadingError || state.photos.isEmpty() -> State.InitialLoading
                    else -> State.LoadingNext(state.photos, state.pageNumber)
                }
                is Effect.LoadingFailed -> when (state) {
                    is State.InitialLoading -> State.InitialLoadingError
                    else -> State.LoadingNextError(state.photos, state.pageNumber)
                }
                is Effect.PhotosCleared -> State.InitialLoading
            }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? =
            null
    }
}
