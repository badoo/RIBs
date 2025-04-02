package com.badoo.ribs.sandbox.rib.dialog_example

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.sandbox.rib.dialog_example.dialog.Dialogs
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleChildBuilders
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter.Configuration
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter.Configuration.Content

class DialogExampleBuilder(
    private val dependency: DialogExample.Dependency
) : SimpleBuilder<DialogExample>() {

    private val builders by lazy { DialogExampleChildBuilders(dependency) }
    private val dialogs = Dialogs(builders)

    override fun build(buildParams: BuildParams<Nothing?>): DialogExample {
        val backStack = BackStack<Configuration>(
            buildParams = buildParams,
            initialConfiguration = Content.Default
        )
        val interactor = interactor(
            buildParams = buildParams,
            backStack = backStack
        )
        val router = router(
            buildParams = buildParams,
            routingSource = backStack
        )
        return node(
            buildParams = buildParams,
            customisation = buildParams.getOrDefault(DialogExample.Customisation()),
            plugins = listOf(
                interactor,
                router
            )
        )
    }

    internal fun router(
        buildParams: BuildParams<Nothing?>,
        routingSource: RoutingSource<Configuration>
    ): DialogExampleRouter =
        DialogExampleRouter(
            buildParams = buildParams,
            routingSource = routingSource,
            dialogLauncher = dependency.dialogLauncher,
            dialogs = dialogs
        )

    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStack<Configuration>
    ): DialogExampleInteractor =
        DialogExampleInteractor(
            buildParams = buildParams,
            backStack = backStack,
            dialogs = dialogs
        )

    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: DialogExample.Customisation,
        plugins: List<Plugin>
    ): DialogExampleNode = DialogExampleNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = plugins
    )
}
