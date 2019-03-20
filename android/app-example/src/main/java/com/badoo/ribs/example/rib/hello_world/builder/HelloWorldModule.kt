package com.badoo.ribs.example.rib.hello_world.builder

import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorld.Input
import com.badoo.ribs.example.rib.hello_world.HelloWorld.Output
import com.badoo.ribs.example.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.example.rib.hello_world.HelloWorldRouter
import com.badoo.ribs.example.rib.hello_world.HelloWorldView
import com.badoo.ribs.example.rib.hello_world.dialog.SimpleDialog
import com.badoo.ribs.example.rib.hello_world.dialog.SomeRibDialog
import com.badoo.ribs.example.rib.hello_world.feature.HelloWorldFeature
import com.badoo.ribs.example.rib.lorem_ipsum.builder.LoremIpsumBuilder
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun dialog(): SimpleDialog =
        SimpleDialog()

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun ribDialog(
        component: HelloWorldComponent
    ): SomeRibDialog =
        SomeRibDialog(
            loremIpsumBuilder = LoremIpsumBuilder(component)
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun router(
        dialogLauncher: DialogLauncher,
        simpleDialog: SimpleDialog,
        someRibDialog: SomeRibDialog
    ): HelloWorldRouter =
        HelloWorldRouter(
            dialogLauncher = dialogLauncher,
            simpleDialog = simpleDialog,
            someRibDialog = someRibDialog
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun feature(): HelloWorldFeature =
        HelloWorldFeature()

    @HelloWorldScope
    @Provides
    @JvmStatic
    @SuppressWarnings("LongParameterList")
    internal fun interactor(
        router: HelloWorldRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: HelloWorldFeature,
        activityStarter: ActivityStarter,
        simpleDialog: SimpleDialog,
        someRibDialog: SomeRibDialog
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            router = router,
            input = input,
            output = output,
            feature = feature,
            activityStarter = activityStarter,
            simpleDialog = simpleDialog,
            someRibDialog = someRibDialog
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        viewFactory: ViewFactory<HelloWorldView>,
        router: HelloWorldRouter,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = Node(
        identifier = object : HelloWorld {},
        viewFactory = viewFactory,
        router = router,
        interactor = interactor
    )
}
