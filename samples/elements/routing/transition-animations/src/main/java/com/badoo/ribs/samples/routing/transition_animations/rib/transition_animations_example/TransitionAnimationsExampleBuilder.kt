package com.badoo.ribs.samples.routing.transition_animations.rib.transition_animations_example

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.routing.transition_animations.rib.transition_animations_example.routing.TransitionAnimationsExampleChildBuilders
import com.badoo.ribs.samples.routing.transition_animations.rib.transition_animations_example.routing.TransitionAnimationsExampleRouter
import com.badoo.ribs.samples.routing.transition_animations.rib.transition_animations_example.routing.TransitionAnimationsExampleRouter.Configuration
import com.badoo.ribs.samples.routing.transition_animations.rib.transition_animations_example.routing.TransitionAnimationsExampleTransitionHandler

class TransitionAnimationsExampleBuilder(
    private val dependency: TransitionAnimationsExample.Dependency
) : SimpleBuilder<TransitionAnimationsExample>() {

    private val builders = TransitionAnimationsExampleChildBuilders()

    override fun build(buildParams: BuildParams<Nothing?>): TransitionAnimationsExample {
        val backStack = backStack(buildParams)
        val transitionHandler = TransitionAnimationsExampleTransitionHandler()
        val router = router(buildParams, backStack, transitionHandler)
        val presenter = presenter(backStack)
        val viewDependency = object : TransitionAnimationsExampleView.Dependency {
            override val presenter: TransitionAnimationsExamplePresenter = presenter
        }

        return node(buildParams, viewDependency, router)
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
    ): TransitionAnimationsExampleRouter =
        TransitionAnimationsExampleRouter(
            buildParams = buildParams,
            routingSource = backStack,
            builders = builders,
            transitionHandler = transitionHandler
        )

    private fun presenter(backStack: BackStack<Configuration>): TransitionAnimationsExamplePresenterImpl =
        TransitionAnimationsExamplePresenterImpl(backStack)

    private fun node(
        buildParams: BuildParams<Nothing?>,
        viewDependency: TransitionAnimationsExampleView.Dependency,
        router: TransitionAnimationsExampleRouter
    ) =
        TransitionAnimationsExampleNode(
            buildParams = buildParams,
            viewFactory = TransitionAnimationsExampleViewImpl.Factory().invoke(viewDependency),
            plugins = listOfNotNull(
                router
            )
        )
}
