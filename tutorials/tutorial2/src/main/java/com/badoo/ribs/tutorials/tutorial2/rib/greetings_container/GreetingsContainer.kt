package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container

import com.badoo.ribs.core.Rib
import io.reactivex.functions.Consumer

interface GreetingsContainer : Rib {

    interface Dependency {
        fun greetingsContainerOutput(): Consumer<Output>
    }

    sealed class Output {
        data class GreetingsSaid(val greeting: String) : Output()
    }
}
