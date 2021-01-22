package com.badoo.ribs.samples.back_stack.parent

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.back_stack.parent.routing.ParentChildBuilders
import com.badoo.ribs.samples.back_stack.parent.routing.ParentRouter
import com.badoo.ribs.samples.back_stack.parent.routing.ParentRouter.Configuration
import com.badoo.ribs.samples.back_stack.parent.routing.ParentRouter.Configuration.Content

class ParentBuilder(
    private val dependency: Parent.Dependency
) : SimpleBuilder<Parent>() {

    private val builders by lazy { ParentChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): Parent {
        val backStack  = backStack(buildParams)
        val router = router(buildParams, backStack)
        val presenter = presenter(backStack)

        val viewDependency = object : ParentView.Dependency {
            override val presenter: ParentPresenter = presenter
        }

        return ParentNode(
            buildParams = buildParams,
            viewFactory = ParentViewImpl.Factory().invoke(viewDependency),
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
    ): ParentRouter =
        ParentRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders
        )

    private fun presenter(backStack: BackStack<Configuration>): ParentPresenterImpl =
        ParentPresenterImpl(backStack)

}
