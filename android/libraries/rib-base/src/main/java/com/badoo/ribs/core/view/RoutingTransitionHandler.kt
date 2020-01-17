package com.badoo.ribs.core.view

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.TransitionDirection.Enter.*
import com.badoo.ribs.core.view.TransitionDirection.Exit.*

// FRAMEWORK

sealed class TransitionElement<C> {
    abstract val configuration: C
    abstract val parentViewGroup: ViewGroup
    abstract val identifier: Rib
    abstract val view: View

    data class Enter<C>(
        override val configuration: C,
        override val parentViewGroup: ViewGroup,
        override val identifier: Rib,
        override val view: View,
        val direction: TransitionDirection.Enter
    ) : TransitionElement<C>()

    data class Exit<C>(
        override val configuration: C,
        override val parentViewGroup: ViewGroup,
        override val identifier: Rib,
        override val view: View,
        val direction: TransitionDirection.Exit
    ) : TransitionElement<C>()
}

//internal sealed class TransitionGeneralDirection {
//    object Enter : TransitionGeneralDirection()
//    object Exit : TransitionGeneralDirection()
//}

sealed class TransitionDirection {
    sealed class Enter : TransitionDirection() {
        object JustCreated : Enter()
        object RestoredFromBackStack: Enter()

    }
    sealed class Exit : TransitionDirection() {
        object Destroyed : Exit()
        object ToBackStack : Exit()

    }
}

//sealed class TransitionDescriptor<C> {
////    abstract val configuration: C
//    abstract val elements: List<TransitionElement<C>>
//
//    data class Enter<C>(
////        override val parentViewGroup: ViewGroup,
////        override val configuration: C,
//        override val elements: List<TransitionElement<C>>,
//        val direction: TransitionDirection.Enter
//    ) : TransitionDescriptor<C>()
//
//    data class Exit<C>(
////        override val parentViewGroup: ViewGroup,
////        override val configuration: C,
//        override val elements: List<TransitionElement<C>>,
//        val direction: TransitionDirection.Exit
//    ) : TransitionDescriptor<C>()
//
//    fun find(identifier: Rib): TransitionElement<C>? =
//        elements.find { identifier == identifier }
//}


// You could have an implementation of this passed to Node constructor
// and if you don't, framework will use DEFAULT
interface RoutingTransitionHandler<C> {

    fun onTransition(
        exit: List<TransitionElement.Exit<out C>>,
        enter: List<TransitionElement.Enter<out C>>,
        onFinished: () -> Unit
    )

    companion object {
        val DEFAULT = object : RoutingTransitionHandler<Any> {
            override fun onTransition(
                exit: List<TransitionElement.Exit<out Any>>,
                enter: List<TransitionElement.Enter<out Any>>,
                onFinished: () -> Unit
            ) {
                onFinished()
            }
        }
    }
}


// CLIENT CODE

sealed class SomeRouterConfiguration {
    object Routing1 : SomeRouterConfiguration()
    object Routing2 : SomeRouterConfiguration()
}

class SomeRoutingTransitionHandlerImpl : RoutingTransitionHandler<SomeRouterConfiguration> {
    override fun onTransition(
        exiting: List<TransitionElement.Exit<out SomeRouterConfiguration>>,
        entering: List<TransitionElement.Enter<out SomeRouterConfiguration>>,
        onFinished: () -> Unit
    ) {
        with(exiting) {
            find { it.identifier == object : Rib {}}?.let {
                // As fine grained control as you wish
//                it.parentViewGroup
//                it.view
//                it.direction
//                it.configuration

                when (it.direction) {
                    Destroyed -> TODO()
                    ToBackStack -> TODO()
                }
                // do some animation stuff
                // set animation finished listener to call:
                onFinished()
            }
        }

        // We could also create nice DSL for cases when you don't need all the custom magic above
//        RibName {
//            ENTER to Fade()
//            EXIT to
//        }
    }
}
