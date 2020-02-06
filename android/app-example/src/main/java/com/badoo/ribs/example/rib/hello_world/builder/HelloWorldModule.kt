package com.badoo.ribs.example.rib.hello_world.builder

import com.badoo.ribs.core.BuildParams
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorld.Input
import com.badoo.ribs.example.rib.hello_world.HelloWorld.Output
import com.badoo.ribs.example.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.example.rib.hello_world.HelloWorldNode
import com.badoo.ribs.example.rib.hello_world.HelloWorldRouter
import com.badoo.ribs.example.rib.hello_world.feature.HelloWorldFeature
import com.badoo.ribs.example.rib.small.builder.SmallBuilder
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun router(
        component: HelloWorldComponent,
        buildParams: BuildParams<Nothing?>
    ): HelloWorldRouter =
        HelloWorldRouter(
            buildParams = buildParams,
            smallBuilder = SmallBuilder(component)
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
        buildParams: BuildParams<Nothing?>,
        router: HelloWorldRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: HelloWorldFeature,
        activityStarter: ActivityStarter
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            buildParams = buildParams,
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
        buildParams: BuildParams<Nothing?>,
        customisation: HelloWorld.Customisation,
        router: HelloWorldRouter,
        interactor: HelloWorldInteractor
    ) : HelloWorldNode = HelloWorldNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )
}
