package com.badoo.ribs.example.rib.switcher.feature

//import com.badoo.ribs.core.LifecycleParent
//import com.badoo.ribs.core.routing.portal.Portal.Sink.Command
//import com.badoo.ribs.core.routing.portal.Portal.Sink.Command.Add
//import com.badoo.ribs.core.routing.portal.Portal.Sink.Command.Remove
//import com.badoo.ribs.core.routing.portal.Portal.Source.Model
//import com.badoo.ribs.core.routing.portal.Portal.Source.Model.Showing
//import com.badoo.ribs.example.rib.switcher.feature.PortalFeature.Effect
//import com.badoo.ribs.example.rib.switcher.feature.PortalFeature.Effect.Added
//import com.badoo.ribs.example.rib.switcher.feature.PortalFeature.Effect.Removed
//import com.badoo.ribs.example.rib.switcher.feature.PortalFeature.State

// FIXME move somewhere else
//class PortalFeature(
//    lifecycleParent: Provider<out LifecycleParent>
//): ActorReducerFeature<Command, Effect, State, Nothing>(
//    initialState = State(),
//    actor = ActorImpl(lifecycleParent),
//    reducer = ReducerImpl()
//), Portal.Sink, Portal.Source {
//
//    data class State(
//        val nodes: List<Node<*>> = emptyList()
//    )
//
//    sealed class Effect {
//        data class Added(val node: Node<*>) : Effect()
//        data class Removed(val node: Node<*>) : Effect()
//    }
//
//    class ActorImpl(
//        private val lifecycleParent: Provider<out LifecycleParent>
//    ) : Actor<State, Command, Effect> {
//        override fun invoke(state: State, command: Command): Observable<out Effect> = when (command) {
//            is Add -> if (state.nodes.contains(command.node)) empty() else
//                just(Added(command.node)).doOnNext {
//                    state.nodes.lastOrNull()?.let {
//                        it.detachFromView()
//                        lifecycleParent.get().removeManaged(it)
//                    }
//                    lifecycleParent.get().addManaged(it.node)
//                }
//
//            is Remove -> if (!state.nodes.contains(command.node)) empty() else
//                just(Removed(command.node)).doOnNext {
//                    it.node.detachFromView()
//                    lifecycleParent.get().removeManaged(it.node)
//                }
//        }
//    }
//
//    class ReducerImpl : Reducer<State, Effect> {
//        override fun invoke(state: State, effect: Effect): State = when (effect) {
//            is Added -> state.copy(nodes = state.nodes + effect.node)
//            is Removed -> state.copy(nodes = state.nodes - effect.node)
//        }
//    }
//
//    override val models: ObservableSource<Model>
//        get() = Observable.wrap(this).map {
//            when {
//                it.nodes.isEmpty() -> Model.Empty
//                else -> Showing(it.nodes.last())
//            }
//        }
//}
