package com.badoo.ribs.tutorials.tutorial4.rib.hello_world.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.HelloWorldView
import com.badoo.ribs.tutorials.tutorial4.util.User
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        user: User,
        config: HelloWorld.Config,
        output: Consumer<HelloWorld.Output>
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            buildParams = buildParams,
            user = user,
            config = config,
            output = output
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
        plugins = listOf(interactor)
    )
}
