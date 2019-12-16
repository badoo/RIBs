package com.badoo.ribs.tutorials.tutorial3.rib.hello_world.builder

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.HelloWorldView
import dagger.Provides

@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            savedInstanceState = savedInstanceState
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: HelloWorld.Customisation,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = Node(
        savedInstanceState = savedInstanceState,
        identifier = object : HelloWorld {},
        viewFactory = customisation.viewFactory(null),
        router = null,
        interactor = interactor
    )
}
