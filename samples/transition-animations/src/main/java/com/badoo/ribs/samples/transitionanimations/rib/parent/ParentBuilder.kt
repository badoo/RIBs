package com.badoo.ribs.samples.transitionanimations.rib.parent

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentChildBuilders
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter.Configuration

class ParentBuilder(
        private val dependency: Parent.Dependency
) : SimpleBuilder<Parent>() {

    private val builders = ParentChildBuilders(dependency)

    override fun build(buildParams: BuildParams<Nothing?>): Parent {
        val customisation = buildParams.getOrDefault(Parent.Customisation())
        val backStack = backStack(buildParams)
        val router = router(buildParams, backStack)
        val presenter = presenter(backStack)
        val viewDependency = object : ParentView.Dependency {
            override val presenter: ParentPresenter = presenter
        }

        return node(buildParams, customisation, viewDependency, router)
    }

    private fun backStack(buildParams: BuildParams<Nothing?>): BackStack<Configuration> =
            BackStack(
                    buildParams = buildParams,
                    initialConfiguration = Configuration.Child1
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

    private fun node(
            buildParams: BuildParams<Nothing?>,
            customisation: Parent.Customisation,
            viewDependency: ParentView.Dependency,
            router: ParentRouter

    ) =
            ParentNode(
                    buildParams = buildParams,
                    viewFactory = customisation.viewFactory(viewDependency),
                    plugins = listOfNotNull(
                            router
                    )
            )
}
