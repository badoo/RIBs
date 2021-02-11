package com.badoo.ribs.samples.dialogs.rib.dialogs_example

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.samples.dialogs.dialogs.Dialogs

class DialogsBuilder(
    private val deps: DialogsExample.Dependency
) : SimpleBuilder<DialogsExample>() {

    private val dialogs = Dialogs()

    override fun build(buildParams: BuildParams<Nothing?>): DialogsExample {
        val interactor = interactor(buildParams = buildParams)
        val router = router(
            buildParams = buildParams,
            interactor = interactor
        )
        return node(
            buildParams = buildParams,
            customisation = buildParams.getOrDefault(DialogsExample.Customisation()),
            plugins = listOf(interactor, router)
        )
    }

    private fun router(
        buildParams: BuildParams<Nothing?>,
        interactor: DialogsInteractor
    ): DialogsRouter =
        DialogsRouter(
            buildParams = buildParams,
            routingSource = interactor,
            dialogLauncher = deps.dialogLauncher,
            dialogs = dialogs
        )

    private fun interactor(buildParams: BuildParams<Nothing?>): DialogsInteractor =
        DialogsInteractor(buildParams = buildParams, dialogs = dialogs)

    private fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: DialogsExample.Customisation,
        plugins: List<Plugin>
    ): DialogsNode = DialogsNode(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = plugins
    )
}
