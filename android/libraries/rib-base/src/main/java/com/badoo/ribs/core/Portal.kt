package com.badoo.ribs.core

import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface Portal {

    interface Sink : Consumer<Sink.Command> {

        sealed class Command {
            data class Add(val node: Node<*>) : Command()
            data class Remove(val node: Node<*>) : Command()
        }
    }

    interface Source {
        val models: ObservableSource<Model>

        sealed class Model {
            object Empty : Model()
            data class Showing(val node: Node<*>) : Model()
        }
    }

    interface Renderer : Consumer<Source.Model>
}
