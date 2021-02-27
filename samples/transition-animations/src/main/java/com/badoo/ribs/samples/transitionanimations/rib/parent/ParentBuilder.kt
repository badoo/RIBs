package com.badoo.ribs.samples.transitionanimations.rib.parent

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentChildBuilders
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentRouter.Configuration
import com.badoo.ribs.samples.transitionanimations.rib.parent.routing.ParentTransitionHandler

class ParentBuilder(
        private val dependency: Parent.Dependency
) : SimpleBuilder<Parent>() {

    private val builders = ParentChildBuilders(dependency)


    override fun build(buildParams: BuildParams<Nothing?>): Parent {
        val customisation = buildParams.getOrDefault(Parent.Customisation())
        val backStack = backStack(buildParams)
        val transitionHandler = ParentTransitionHandler()
        val router = router(buildParams, backStack, transitionHandler)
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
            backStack: BackStack<Configuration>,
            transitionHandler: TransitionHandler<Configuration>
    ): ParentRouter =
            ParentRouter(
                    buildParams = buildParams,
                    routingSource = backStack,
                    builders = builders,
                    transitionHandler = transitionHandler
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
