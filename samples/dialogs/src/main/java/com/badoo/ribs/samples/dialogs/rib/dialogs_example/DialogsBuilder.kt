package com.badoo.ribs.samples.dialogs.rib.dialogs_example

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.dialogs.dialogs.Dialogs
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsRouter.Configuration.Content

class DialogsBuilder(
    private val deps: DialogsExample.Dependency
) : SimpleBuilder<DialogsExample>() {

    private val dialogs = Dialogs()

    override fun build(buildParams: BuildParams<Nothing?>): DialogsExample {
        val backStack: BackStack<DialogsRouter.Configuration> = BackStack(
            initialConfiguration = Content.Default,
            buildParams = buildParams
        )
        val presenter = DialogsPresenterImpl(dialogs, backStack)
        val router = DialogsRouter(
            buildParams = buildParams,
            routingSource = backStack,
            dialogLauncher = deps.dialogLauncher,
            dialogs = dialogs
        )
        val viewDependencies = object : DialogsView.Dependency {
            override val presenter: DialogsPresenter = presenter
        }
        return DialogsNode(
            buildParams = buildParams,
            viewFactory = DialogsViewImpl.Factory().invoke(viewDependencies),
            plugins = listOf(router, presenter)
        )
    }
}
