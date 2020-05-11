package com.badoo.ribs.tutorials.tutorial5.rib.greetings_container.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial5.R
import com.badoo.ribs.tutorials.tutorial5.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial5.rib.greetings_container.GreetingsContainerInteractor
import com.badoo.ribs.tutorials.tutorial5.rib.greetings_container.GreetingsContainerRouter
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.builder.HelloWorldBuilder
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelector
import com.badoo.ribs.android.Text
import com.badoo.ribs.core.builder.BuildParams
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object GreetingsContainerModule {

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: GreetingsContainerComponent,
        buildParams: BuildParams<Nothing?>
    ): GreetingsContainerRouter =
        GreetingsContainerRouter(
            buildParams = buildParams,
            helloWorldBuilder = HelloWorldBuilder(component)
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: GreetingsContainerRouter,
        output: Consumer<GreetingsContainer.Output>
    ): GreetingsContainerInteractor =
        GreetingsContainerInteractor(
            buildParams = buildParams,
            router = router,
            output = output
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun lexemes(): List<@JvmSuppressWildcards Text> =
        listOf(
            Text.Plain("Hello"),
            Text.Plain("Grüss gott"),
            Text.Plain("Bonjour"),
            Text.Plain("Hola"),
            Text.Plain("Szép jó napot"),
            Text.Plain("Góðan dag")
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        router: GreetingsContainerRouter,
        interactor: GreetingsContainerInteractor
    ) : Node<Nothing> = Node(
        buildParams = buildParams,
        viewFactory = null,
        plugins = listOf(interactor, router)
    )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun helloWorldConfig(
        options: @JvmSuppressWildcards List<Text>
    ): HelloWorld.Config =
        HelloWorld.Config(
            welcomeMessage = Text.Resource(R.string.hello_world_welcome_text),
            buttonText = options.first()
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun helloWorldOutputConsumer(
        interactor: GreetingsContainerInteractor
    ) : Consumer<HelloWorld.Output> =
        interactor.helloWorldOutputConsumer

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun helloWorldInputSource(
        interactor: GreetingsContainerInteractor
    ) : ObservableSource<HelloWorld.Input> =
        interactor.helloWorldInputSource

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun moreOptionsConfig(
        options: @JvmSuppressWildcards List<Text>
    ): OptionSelector.Config =
        OptionSelector.Config(
            options = options
        )

    @GreetingsContainerScope
    @Provides
    @JvmStatic
    internal fun moreOptionsOutput(
        interactor: GreetingsContainerInteractor
    ): Consumer<OptionSelector.Output> =
        interactor.optionsSelectorOutputConsumer
}
