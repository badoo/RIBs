package com.badoo.ribs.samples.back_stack.rib.back_stack_example

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.routing.BackStackExampleChildBuilders
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.routing.BackStackExampleRouter
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.routing.BackStackExampleRouter.Configuration
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.routing.BackStackExampleRouter.Configuration.Content

class BackStackExampleBuilder(
    private val dependency: BackStackExample.Dependency
) : SimpleBuilder<BackStackExample>() {

    private val builders by lazy { BackStackExampleChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): BackStackExample {
        val backStack  = backStack(buildParams)
        val router = router(buildParams, backStack)
        val presenter = presenter(backStack)

        val viewDependency = object : BackStackExampleView.Dependency {
            override val presenter: BackStackExamplePresenter = presenter
        }

        return BackStackExampleNode(
            buildParams = buildParams,
            viewFactory = BackStackExampleViewImpl.Factory().invoke(viewDependency),
            plugins = listOfNotNull(
                router,
                presenter
            )
        )
    }

    private fun backStack(buildParams: BuildParams<Nothing?>): BackStack<Configuration> =
        BackStack(
            buildParams = buildParams,
            initialConfiguration = Content.A
        )

    private fun router(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStack<Configuration>
    ): BackStackExampleRouter =
        BackStackExampleRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders
        )

    private fun presenter(backStack: BackStack<Configuration>): BackStackExamplePresenterImpl =
        BackStackExamplePresenterImpl(backStack)

}
