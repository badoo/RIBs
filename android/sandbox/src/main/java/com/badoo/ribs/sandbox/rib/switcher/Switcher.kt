package com.badoo.ribs.sandbox.rib.switcher

import com.badoo.ribs.android.CanProvideActivityStarter
import com.badoo.ribs.android.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.transition.handler.TransitionHandler
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.customisation.RibCustomisation
import com.badoo.ribs.dialog.CanProvideDialogLauncher
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld
import com.badoo.ribs.sandbox.rib.switcher.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.util.CoffeeMachine
import io.reactivex.Single

interface Switcher : Rib {

    interface Dependency :
        CanProvideActivityStarter,
        CanProvidePermissionRequester,
        CanProvideDialogLauncher,
        CanProvidePortal {

        fun coffeeMachine(): CoffeeMachine
    }

    class Customisation(
        val viewFactory: SwitcherView.Factory = SwitcherViewImpl.Factory(),
        val transitionHandler: TransitionHandler<Configuration> = SwitcherTransitionHandler(duration = 2000)
    ) : RibCustomisation

    // Workflow
    fun attachHelloWorld(): Single<HelloWorld>
    fun testCrash(): Single<HelloWorld>
    fun waitForHelloWorld(): Single<HelloWorld>
    fun doSomethingAndStayOnThisNode(): Single<Switcher>
}
