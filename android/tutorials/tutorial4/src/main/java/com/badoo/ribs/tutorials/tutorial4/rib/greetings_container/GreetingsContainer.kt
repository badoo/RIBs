package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container

import com.badoo.ribs.core.Rib
import com.badoo.ribs.tutorials.tutorial4.util.User
import io.reactivex.functions.Consumer

interface GreetingsContainer : Rib<GreetingsContainerView> {

    interface Dependency {
        fun user(): User
        fun greetingsContainerOutput(): Consumer<Output>
    }

    sealed class Output {
        data class GreetingsSaid(val greeting: String) : Output()
    }
}
