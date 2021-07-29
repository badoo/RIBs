package com.badoo.ribs.samples.dialogs.rib.dialogs_example

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.dialogs.dialogs.Dialogs
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsExampleRouter.Configuration.Content

class DialogsExampleBuilder(
    private val deps: DialogsExample.Dependency
) : SimpleBuilder<DialogsExample>() {

    private val dialogs = Dialogs()

    override fun build(buildParams: BuildParams<Nothing?>): DialogsExample {
        val backStack: BackStack<DialogsExampleRouter.Configuration> = BackStack(
            initialConfiguration = Content.Default,
            buildParams = buildParams
        )
        val presenter = DialogsExamplePresenterImpl(dialogs, backStack)
        val router = DialogsExampleRouter(
            buildParams = buildParams,
            routingSource = backStack,
            dialogLauncher = deps.dialogLauncher,
            dialogs = dialogs
        )
        val viewDependencies = object : DialogsExampleView.Dependency {
            override val presenter: DialogsExamplePresenter = presenter
        }
        return DialogsExampleNode(
            buildParams = buildParams,
            viewFactory = DialogsExampleViewImpl.Factory().invoke(viewDependencies),
            plugins = listOf(router, presenter)
        )
    }
}
