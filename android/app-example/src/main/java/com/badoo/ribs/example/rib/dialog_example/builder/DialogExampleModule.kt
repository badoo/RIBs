@file:Suppress("LongParameterList")
package com.badoo.ribs.example.rib.dialog_example.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Node
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.dialog_example.DialogExampleInteractor
import com.badoo.ribs.example.rib.dialog_example.DialogExampleRouter
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView
import com.badoo.ribs.example.rib.dialog_example.dialog.LazyDialog
import com.badoo.ribs.example.rib.dialog_example.dialog.RibDialog
import com.badoo.ribs.example.rib.dialog_example.dialog.SimpleDialog
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.lorem_ipsum.builder.LoremIpsumBuilder
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object DialogExampleModule {

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun simpleDialog(): SimpleDialog =
        SimpleDialog()

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun lazyDialog(): LazyDialog =
        LazyDialog()

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun ribDialog(
        component: DialogExampleComponent
    ): RibDialog =
        RibDialog(
            loremIpsumBuilder = LoremIpsumBuilder(component)
        )

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun router(
        buildContext: BuildContext<Nothing?>,
        dialogLauncher: DialogLauncher,
        simpleDialog: SimpleDialog,
        lazyDialog: LazyDialog,
        ribDialog: RibDialog
    ): DialogExampleRouter =
        DialogExampleRouter(
            buildContext = buildContext,
            dialogLauncher = dialogLauncher,
            simpleDialog = simpleDialog,
            lazyDialog = lazyDialog,
            ribDialog = ribDialog
        )

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildContext: BuildContext<Nothing?>,
        router: DialogExampleRouter,
        simpleDialog: SimpleDialog,
        lazyDialog: LazyDialog,
        ribDialog: RibDialog
    ): DialogExampleInteractor =
        DialogExampleInteractor(
            buildContext = buildContext,
            router = router,
            simpleDialog = simpleDialog,
            lazyDialog = lazyDialog,
            ribDialog = ribDialog
        )

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun node(
        buildContext: BuildContext<Nothing?>,
        customisation: DialogExample.Customisation,
        router: DialogExampleRouter,
        interactor: DialogExampleInteractor
    ) : Node<DialogExampleView> = Node(
        buildContext = buildContext,
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun loremIpsumOutputConsumer(
        interactor: DialogExampleInteractor
    ): Consumer<LoremIpsum.Output> =
        interactor.loremIpsumOutputConsumer
}
