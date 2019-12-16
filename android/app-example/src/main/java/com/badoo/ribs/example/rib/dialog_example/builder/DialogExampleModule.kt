@file:Suppress("LongParameterList")
package com.badoo.ribs.example.rib.dialog_example.builder

import android.os.Bundle
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
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumBuilder
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
        savedInstanceState: Bundle?,
        dialogLauncher: DialogLauncher,
        simpleDialog: SimpleDialog,
        lazyDialog: LazyDialog,
        ribDialog: RibDialog
    ): DialogExampleRouter =
        DialogExampleRouter(
            savedInstanceState = savedInstanceState,
            dialogLauncher = dialogLauncher,
            simpleDialog = simpleDialog,
            lazyDialog = lazyDialog,
            ribDialog = ribDialog
        )

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: DialogExampleRouter,
        simpleDialog: SimpleDialog,
        lazyDialog: LazyDialog,
        ribDialog: RibDialog
    ): DialogExampleInteractor =
        DialogExampleInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            simpleDialog = simpleDialog,
            lazyDialog = lazyDialog,
            ribDialog = ribDialog
        )

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: DialogExample.Customisation,
        router: DialogExampleRouter,
        interactor: DialogExampleInteractor
    ) : Node<DialogExampleView> = Node(
        savedInstanceState = savedInstanceState,
        identifier = object : DialogExample {},
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
