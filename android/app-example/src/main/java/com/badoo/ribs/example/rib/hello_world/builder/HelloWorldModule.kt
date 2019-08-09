package com.badoo.ribs.example.rib.hello_world.builder

import android.os.Bundle
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorld.Input
import com.badoo.ribs.example.rib.hello_world.HelloWorld.Output
import com.badoo.ribs.example.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.example.rib.hello_world.HelloWorldNode
import com.badoo.ribs.example.rib.hello_world.HelloWorldRouter
import com.badoo.ribs.example.rib.hello_world.feature.HelloWorldFeature
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun router(
        savedInstanceState: Bundle?
    ): HelloWorldRouter =
        HelloWorldRouter(
            savedInstanceState = savedInstanceState
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun feature(): HelloWorldFeature =
        HelloWorldFeature()

    @HelloWorldScope
    @Provides
    @JvmStatic
    @SuppressWarnings("LongParameterList", "LongMethod")
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: HelloWorldRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: HelloWorldFeature,
        activityStarter: ActivityStarter
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            input = input,
            output = output,
            feature = feature,
            activityStarter = activityStarter
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: HelloWorld.Customisation,
        router: HelloWorldRouter,
        interactor: HelloWorldInteractor
    ) : HelloWorldNode = HelloWorldNode(
        savedInstanceState = savedInstanceState,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
