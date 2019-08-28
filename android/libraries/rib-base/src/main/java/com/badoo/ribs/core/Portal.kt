package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.view.RibView
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface Portal {

    fun push(resolverChain: List<Parcelable>)

//    interface Sink : Consumer<Sink.Command> {
//    interface Sink {

//        val remoteRoutingAction: ((Bundle?) -> Node<*>) -> RoutingAction<out RibView>

//        sealed class Command {
//            data class Add(val node: Node<*>) : Command()
//            data class Remove(val node: Node<*>) : Command()
//        }
//    }

//    interface Source {
//        val models: ObservableSource<Model>
//
//        sealed class Model {
//            object Empty : Model()
//            data class Showing(val node: Node<*>) : Model()
//        }
//    }
//
//    interface Renderer : Consumer<Source.Model>
}
