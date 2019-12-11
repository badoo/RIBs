@file:Suppress("LongParameterList")
package com.badoo.ribs.example.rib.main_dialog_example.builder

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsum
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsumBuilder
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExample
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExampleInteractor
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExampleRouter
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExampleView
import com.badoo.ribs.example.rib.main_dialog_example.dialog.LazyDialog
import com.badoo.ribs.example.rib.main_dialog_example.dialog.RibDialog
import com.badoo.ribs.example.rib.main_dialog_example.dialog.SimpleDialog
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object MainDialogExampleModule {

    @MainDialogExampleScope
    @Provides
    @JvmStatic
    internal fun simpleDialog(): SimpleDialog =
        SimpleDialog()

    @MainDialogExampleScope
    @Provides
    @JvmStatic
    internal fun lazyDialog(): LazyDialog =
        LazyDialog()

    @MainDialogExampleScope
    @Provides
    @JvmStatic
    internal fun ribDialog(
        component: MainDialogExampleComponent
    ): RibDialog =
        RibDialog(
            loremIpsumBuilder = DialogLoremIpsumBuilder(component)
        )

    @MainDialogExampleScope
    @Provides
    @JvmStatic
    internal fun router(
        savedInstanceState: Bundle?,
        dialogLauncher: DialogLauncher,
        simpleDialog: SimpleDialog,
        lazyDialog: LazyDialog,
        ribDialog: RibDialog
    ): MainDialogExampleRouter =
        MainDialogExampleRouter(
            savedInstanceState = savedInstanceState,
            dialogLauncher = dialogLauncher,
            simpleDialog = simpleDialog,
            lazyDialog = lazyDialog,
            ribDialog = ribDialog
        )

    @MainDialogExampleScope
    @Provides
    @JvmStatic
    internal fun interactor(
        savedInstanceState: Bundle?,
        router: MainDialogExampleRouter,
        simpleDialog: SimpleDialog,
        lazyDialog: LazyDialog,
        ribDialog: RibDialog
    ): MainDialogExampleInteractor =
        MainDialogExampleInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            simpleDialog = simpleDialog,
            lazyDialog = lazyDialog,
            ribDialog = ribDialog
        )

    @MainDialogExampleScope
    @Provides
    @JvmStatic
    internal fun node(
        savedInstanceState: Bundle?,
        customisation: MainDialogExample.Customisation,
        router: MainDialogExampleRouter,
        interactor: MainDialogExampleInteractor
    ) : Node<MainDialogExampleView> = Node(
        savedInstanceState = savedInstanceState,
        identifier = object : MainDialogExample {},
        viewFactory = customisation.viewFactory(null),
        router = router,
        interactor = interactor
    )

    @MainDialogExampleScope
    @Provides
    @JvmStatic
    internal fun loremIpsumOutputConsumer(
        interactor: MainDialogExampleInteractor
    ): Consumer<DialogLoremIpsum.Output> =
        interactor.loremIpsumOutputConsumer
}
