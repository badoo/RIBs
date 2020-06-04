package com.badoo.ribs.sandbox.rib.hello_world

import com.badoo.ribs.android.CanProvideActivityStarter
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.portal.CanProvidePortal
import com.badoo.ribs.customisation.RibCustomisation
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld.Input
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld.Output
import io.reactivex.Single

interface HelloWorld : Rib, Connectable<Input, Output> {

    interface Dependency :
        CanProvideActivityStarter,
        CanProvidePortal

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: HelloWorldView.Factory = HelloWorldViewImpl.Factory()
    ) : RibCustomisation

    // Workflow
    fun somethingSomethingDarkSide(): Single<HelloWorld>
}
