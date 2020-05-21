//package com.badoo.ribs.android.recyclerview
//
//import android.os.Parcelable
//import com.badoo.mvicore.element.Bootstrapper
//import com.badoo.mvicore.element.Reducer
//import com.badoo.mvicore.element.TimeCapsule
//import com.badoo.mvicore.feature.ReducerFeature
//import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input
//import com.badoo.ribs.android.recyclerview.RecyclerViewHostFeature.State
//import com.badoo.ribs.android.recyclerview.RecyclerViewHostRouter.Configuration.Content.Item
//import com.badoo.ribs.core.routing.configuration.ConfigurationKey
//import io.reactivex.Observable
//import kotlinx.android.parcel.Parcelize
//import java.util.UUID
//
//private val timeCapsuleKey = "RecyclerViewHostFeature"
//private fun <T : Parcelable> TimeCapsule<State<T>>.initialState(): State<T> =
//    (get(timeCapsuleKey) ?: State())
//
//
//internal class RecyclerViewHostFeature<T : Parcelable>(
//    timeCapsule: TimeCapsule<State<T>>,
//    initialElements: List<T>
//) : ReducerFeature<Input<T>, State<T>, Nothing>(
//    initialState = timeCapsule.initialState(),
//    bootstrapper = BootstrapperImpl(timeCapsule.initialState(), initialElements),
//    reducer = ReducerImpl()
//) {
//    init {
//        timeCapsule.register(timeCapsuleKey) {
//            state.copy(
//                lastCommand = null
//            )
//        }
//    }
//
//    @Parcelize
//    data class State<T : Parcelable>(
//        val nextKey: Int = 1000,
//        val items: List<Entry<T>> = emptyList(),
//        val lastCommand: Input<T>? = null
//    ) : Parcelable {
//
//        @Parcelize
//        data class Entry<T : Parcelable>(
//            val element: T,
//            val uuid: UUID = UUID.randomUUID(),
//            val configurationKey: ConfigurationKey<RecyclerViewHostRouter.Configuration>
//        ) : Parcelable
//    }
//
//    class BootstrapperImpl<T : Parcelable>(
//        private val initialState: State<T>,
//        private val initialElements: List<T>
//    ) : Bootstrapper<Input<T>> {
//        override fun invoke(): Observable<Input<T>> =
//            Observable
//                .fromIterable(if (initialState.items.isEmpty()) initialElements else emptyList())
//                .map {
//                    Input.Add(it)
//                }
//    }
//
//    /**
//     * [ConfigurationKey] uses Int parameter for position. That's convenient for back stack,
//     * but in our case, positions can change in a RecyclerView, and that could cause
//     * [ConfigurationKey] collision easily. Here we mark elements use [State.nextKey] for
//     * [ConfigurationKey] position, which we keep incrementing.
//     */
//    class ReducerImpl<T : Parcelable> : Reducer<State<T>, Input<T>> {
//        override fun invoke(state: State<T>, input: Input<T>): State<T> = when (input) {
//            is Input.Add -> {
//                val uuid = UUID.randomUUID()
//                state.copy(
//                    lastCommand = input,
//                    nextKey = state.nextKey + 1,
//                    items = state.items + State.Entry<T>(
//                        element = input.element,
//                        uuid = uuid,
//                        configurationKey = ConfigurationKey.Content(
//                            index = state.nextKey,
//                            configuration = Item(uuid)
//                        )
//                    )
//                )
//            }
//        }
//    }
//}
