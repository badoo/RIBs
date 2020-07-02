package com.badoo.ribs.example.photo_details.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.example.extensions.toObservable
import com.badoo.ribs.example.network.model.Photo
import com.badoo.ribs.example.photo_details.PhotoDetailsDataSource
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature.Effect
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature.News
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature.State
import com.badoo.ribs.example.photo_details.feature.PhotoDetailsFeature.Wish
import io.reactivex.Observable

internal class PhotoDetailsFeature(
    photoId: String,
    photoDetailsDataSource: PhotoDetailsDataSource
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State.Loading,
    bootstrapper = BootStrapperImpl(),
    actor = ActorImpl(photoDetailsDataSource, photoId),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    sealed class State {
        object Loading : State()
        data class Loaded(val detail: Photo) : State()
    }

    sealed class Wish {
        object LoadPhoto : Wish()
        object LikePhoto : Wish()
    }

    sealed class Effect {
        object LoadingStarted : Effect()
        data class PhotoLoaded(val detail: Photo) : Effect()
        object LikeSent : Effect()
    }

    sealed class News

    class BootStrapperImpl : Bootstrapper<Wish> {
        override fun invoke(): Observable<Wish> = Wish.LoadPhoto.toObservable()
    }

    class ActorImpl(
        private val photoDetailsDataSource: PhotoDetailsDataSource,
        private val photoId: String
    ) : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> =
            when (wish) {
                Wish.LoadPhoto -> photoDetailsDataSource.getPhoto(photoId)
                    .map<Effect> { Effect.PhotoLoaded(it) }
                    .toObservable()
                    .startWith(Effect.LoadingStarted)
                Wish.LikePhoto -> photoDetailsDataSource.likePhoto(photoId)
                    .toObservable<Effect>()
                    .startWith(Effect.LikeSent)
            }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State =
            when (effect) {
                is Effect.LoadingStarted -> State.Loading
                is Effect.PhotoLoaded -> State.Loaded(effect.detail)
                is Effect.LikeSent -> if (state is State.Loaded) {
                    state.copy(detail = state.detail.copy(likedByUser = true))
                } else {
                    state
                }
            }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? =
            null
    }
}
