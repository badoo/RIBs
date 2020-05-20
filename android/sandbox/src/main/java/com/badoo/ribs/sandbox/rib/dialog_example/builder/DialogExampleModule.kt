@file:Suppress("LongParameterList")
package com.badoo.ribs.sandbox.rib.dialog_example.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleInteractor
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleNode
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter
import com.badoo.ribs.sandbox.rib.dialog_example.dialog.LazyDialog
import com.badoo.ribs.sandbox.rib.dialog_example.dialog.RibDialog
import com.badoo.ribs.sandbox.rib.dialog_example.dialog.SimpleDialog
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleConnections
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsumBuilder
import dagger.Provides

@dagger.Module
internal object DialogExampleModule {

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun connections(
        component: DialogExampleComponent
    ) = DialogExampleConnections(
        loremIpsumBuilder = LoremIpsumBuilder(component)
    )

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
        buildParams: BuildParams<Nothing?>,
        dependency: DialogExample.Dependency,
        simpleDialog: SimpleDialog,
        lazyDialog: LazyDialog,
        ribDialog: RibDialog
    ): DialogExampleRouter =
        DialogExampleRouter(
            buildParams = buildParams,
            dialogLauncher = dependency.dialogLauncher(),
            simpleDialog = simpleDialog,
            lazyDialog = lazyDialog,
            ribDialog = ribDialog
        )

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: DialogExampleRouter,
        connections: DialogExampleConnections,
        simpleDialog: SimpleDialog,
        lazyDialog: LazyDialog,
        ribDialog: RibDialog
    ): DialogExampleInteractor =
        DialogExampleInteractor(
            buildParams = buildParams,
            router = router,
            connections = connections,
            simpleDialog = simpleDialog,
            lazyDialog = lazyDialog,
            ribDialog = ribDialog
        )

    @DialogExampleScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: DialogExample.Customisation,
        router: DialogExampleRouter,
        interactor: DialogExampleInteractor
    ) : DialogExampleNode = DialogExampleNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins =  listOf(
            interactor,
            router
        )
    )
}
