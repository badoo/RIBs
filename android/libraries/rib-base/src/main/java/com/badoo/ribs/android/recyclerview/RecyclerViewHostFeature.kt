package com.badoo.ribs.android.recyclerview

import android.os.Parcelable
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ReducerFeature
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input
import com.badoo.ribs.android.recyclerview.RecyclerViewHostFeature.State
import com.badoo.ribs.android.recyclerview.RecyclerViewHostFeature.Wish
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize
import java.util.UUID

private val timeCapsuleKey = RecyclerViewHostFeature::class.java.name
private fun <T : Parcelable> TimeCapsule<State<T>>.initialState(): State<T> =
    (get(timeCapsuleKey) ?: State())


internal class RecyclerViewHostFeature<T : Parcelable>(
    timeCapsule: TimeCapsule<State<T>>,
    initialElements: List<T>
) : ReducerFeature<Wish<T>, State<T>, Nothing>(
    initialState = timeCapsule.initialState(),
    bootstrapper = BootstrapperImpl(timeCapsule.initialState(), initialElements),
    reducer = ReducerImpl()
//    ,
//    newsPublisher = NewsPublisherImpl()
) {
    init {
        timeCapsule.register(timeCapsuleKey) {
            state.copy(
                lastCommand = null
//                items = state.items.map {
//                    it.copy(
////                        processed = false
//                    )
//                }
            )
        }
    }

    @Parcelize
    data class State<T : Parcelable>(
        val nextKey: Int = 1000,
        val items: List<Entry<T>> = listOf(),
        val lastCommand: Input<T>? = null
    ) : Parcelable {

        @Parcelize
        data class Entry<T : Parcelable>(
            val element: T,
//            val processed: Boolean = false, // TODO consider separate type
            val uuid: UUID = UUID.randomUUID(),
            val configurationKey: ConfigurationKey
        ) : Parcelable
    }

    sealed class Wish<T : Parcelable>{
        data class Execute<T : Parcelable>(val input: Input<T>): Wish<T>()
//        data class MarkAsProcessed<T>(val uuids: List<UUID>): Wish<T>()
    }

//    sealed class News<T> {
//        data class Executed<T>(val input: Wish<T>): News<T>()
//    }

    class BootstrapperImpl<T : Parcelable>(
        private val initialState: State<T>,
        private val initialElements: List<T>
    ) : Bootstrapper<Wish<T>> {
        override fun invoke(): Observable<Wish<T>> =
            Observable
                .fromIterable(if (initialState.items.isEmpty()) initialElements else emptyList())
                .map {
                    Wish.Execute(Input.Add(it))
                }

    }

    /**
     * [ConfigurationKey] uses Int parameter for position. That's convenient for back stack,
     * but in our case, positions can change in a RecyclerView, and that could cause
     * [ConfigurationKey] collision easily. Here we mark elements use [State.nextKey] for
     * [ConfigurationKey] position, which we keep incrementing.
     */
    class ReducerImpl<T : Parcelable> : Reducer<State<T>, Wish<T>> {
        override fun invoke(state: State<T>, wish: Wish<T>): State<T> = when (wish) {
            is Wish.Execute -> when (val input = wish.input) {
                is Input.Add -> state.copy(
                    lastCommand = input,
                    nextKey = state.nextKey + 1,
                    items = state.items + State.Entry(
                        element = input.element,
                        uuid = UUID.randomUUID(),
                        configurationKey = ConfigurationKey.Content(
                            state.nextKey
                        )
                    )
                )
            }
//            is Wish.MarkAsProcessed -> state.copy(
//                items = state.items.map {
//                    if (wish.uuids.contains(it.uuid)) it.copy(processed = true) else it
//                }
//            )
        }
    }

//    class NewsPublisherImpl<T : Parcelable> : SimpleNewsPublisher<Wish<T>, State<T>, News<T>>() {
//        override fun invoke(input: Wish<T>, state: State<T>): News<T>? =
//            News.Executed(input)
//    }

}
