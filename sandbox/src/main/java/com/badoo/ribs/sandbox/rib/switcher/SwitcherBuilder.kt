package com.badoo.ribs.sandbox.rib.switcher

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.core.routing.source.backstack.BackStackFeature
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherChildBuilders
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.sandbox.util.CoffeeMachine
import com.badoo.ribs.sandbox.util.StupidCoffeeMachine

class SwitcherBuilder(
    private val dependency: Switcher.Dependency
) : SimpleBuilder<SwitcherNode>() {

    private val builders = SwitcherChildBuilders(dependency)
    private val dialogToTestOverlay = DialogToTestOverlay()
    private val viewDependency: SwitcherView.Dependency =
        object : SwitcherView.Dependency {
            override fun coffeeMachine(): CoffeeMachine = StupidCoffeeMachine()
        }

    override fun build(buildParams: BuildParams<Nothing?>): SwitcherNode {
        val customisation = buildParams.getOrDefault(Switcher.Customisation())
        val backStack  = backStack(buildParams)
        val interactor = interactor(buildParams, backStack)
        val router = router(buildParams, customisation, backStack)

        return SwitcherNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(viewDependency),
            backStack = backStack,
            plugins = listOf(
                interactor,
                router
            )
        )
    }

    private fun backStack(buildParams: BuildParams<Nothing?>): BackStackFeature<Configuration> =
        BackStackFeature(
            buildParams = buildParams,
            initialConfiguration = Content.DialogsExample
        )

    private fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStackFeature<Configuration>
    ): SwitcherInteractor =
        SwitcherInteractor(
            buildParams = buildParams,
            backStack = backStack,
            dialogToTestOverlay = dialogToTestOverlay
        )

    private fun router(
        buildParams: BuildParams<Nothing?>,
        customisation: Switcher.Customisation,
        backStack: BackStackFeature<Configuration>
    ): SwitcherRouter =
        SwitcherRouter(
            buildParams = buildParams,
            routingSource = backStack,
            transitionHandler = customisation.transitionHandler,
            builders = builders,
            dialogLauncher = dependency.dialogLauncher(),
            dialogToTestOverlay = dialogToTestOverlay
        )
}
