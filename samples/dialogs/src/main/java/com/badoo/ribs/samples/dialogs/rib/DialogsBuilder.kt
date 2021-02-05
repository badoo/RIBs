package com.badoo.ribs.samples.dialogs.rib

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.samples.dialogs.dialogtypes.DialogTypes

class DialogsBuilder(
        private val deps: Dialogs.Dependency
) : SimpleBuilder<Dialogs>() {

    private val dialogs = DialogTypes()

    override fun build(buildParams: BuildParams<Nothing?>): Dialogs {
        val interactor = interactor(buildParams = buildParams)
        val router = router(
                buildParams = buildParams,
                interactor = interactor
        )
        return node(
                buildParams = buildParams,
                customisation = buildParams.getOrDefault(Dialogs.Customisation()),
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
            customisation: Dialogs.Customisation,
            plugins: List<Plugin>
    ): DialogsNode = DialogsNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = plugins
    )
}
