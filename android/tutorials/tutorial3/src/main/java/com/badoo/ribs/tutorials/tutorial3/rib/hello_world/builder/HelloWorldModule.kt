package com.badoo.ribs.tutorials.tutorial3.rib.hello_world.builder

import com.badoo.ribs.core.builder.BuildParams
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
        buildParams: BuildParams<Nothing?>
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            buildParams = buildParams
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: HelloWorld.Customisation,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = Node(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = null,
        interactor = interactor
    )
}
