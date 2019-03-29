package com.badoo.ribs.example.rib.hello_world.builder

import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.base.leaf.LeafNode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorld.Input
import com.badoo.ribs.example.rib.hello_world.HelloWorld.Output
import com.badoo.ribs.example.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.example.rib.hello_world.HelloWorldView
import com.badoo.ribs.example.rib.hello_world.feature.HelloWorldFeature
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object HelloWorldModule {

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
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: HelloWorldFeature,
        activityStarter: ActivityStarter
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            input = input,
            output = output,
            feature = feature,
            activityStarter = activityStarter
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        viewFactory: ViewFactory<HelloWorldView>,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = LeafNode(
        identifier = object : HelloWorld {},
        viewFactory = viewFactory,
        interactor = interactor
    )
}
