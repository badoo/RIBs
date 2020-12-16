package com.badoo.ribs.sandbox.rib.dialog_example

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.sandbox.rib.dialog_example.dialog.Dialogs
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleChildBuilders
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter

class DialogExampleBuilder(
    private val dependency: DialogExample.Dependency
) : SimpleBuilder<DialogExample>() {

    private val builders by lazy { DialogExampleChildBuilders(dependency) }
    private val dialogs = Dialogs(builders)

    override fun build(buildParams: BuildParams<Nothing?>): DialogExample {
        val interactor = interactor(
            buildParams = buildParams
        )
        val router = router(
            buildParams = buildParams,
            interactor = interactor
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
        interactor: DialogExampleInteractor
    ): DialogExampleRouter =
        DialogExampleRouter(
            buildParams = buildParams,
            routingSource = interactor,
            dialogLauncher = dependency.dialogLauncher,
            dialogs = dialogs
        )

    internal fun interactor(
        buildParams: BuildParams<Nothing?>
    ): DialogExampleInteractor =
        DialogExampleInteractor(
            buildParams = buildParams,
            dialogs = dialogs
        )

    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: DialogExample.Customisation,
        plugins: List<Plugin>
    ) : DialogExampleNode = DialogExampleNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = plugins
    )
}
