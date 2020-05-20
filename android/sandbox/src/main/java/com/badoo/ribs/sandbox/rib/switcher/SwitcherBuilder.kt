package com.badoo.ribs.sandbox.rib.switcher

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.sandbox.rib.switcher.dialog.DialogToTestOverlay
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherConnections
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter
import com.badoo.ribs.sandbox.util.CoffeeMachine
import com.badoo.ribs.sandbox.util.StupidCoffeeMachine

class SwitcherBuilder(
    private val dependency: Switcher.Dependency
) : SimpleBuilder<SwitcherNode>() {

    private val children = SwitcherConnections(dependency)
    private val dialogToTestOverlay = DialogToTestOverlay()
    private val viewDependency: SwitcherView.Dependency =
        object : SwitcherView.Dependency {
            override fun coffeeMachine(): CoffeeMachine = StupidCoffeeMachine()
        }

    override fun build(buildParams: BuildParams<Nothing?>): SwitcherNode {
        val customisation = buildParams.getOrDefault(Switcher.Customisation())
        val router = router(buildParams, customisation)
        val interactor = interactor(buildParams, router)

        return SwitcherNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(viewDependency),
            router = router,
            plugins = listOf(
                interactor,
                router
            )
        )
    }

    private fun router(
        buildParams: BuildParams<Nothing?>,
        customisation: Switcher.Customisation
    ): SwitcherRouter =
        SwitcherRouter(
            buildParams = buildParams,
            transitionHandler = customisation.transitionHandler,
            connections = children,
            dialogLauncher = dependency.dialogLauncher(),
            dialogToTestOverlay = dialogToTestOverlay
        )

    private fun interactor(
        buildParams: BuildParams<Nothing?>,
        router: SwitcherRouter
    ): SwitcherInteractor =
        SwitcherInteractor(
            buildParams = buildParams,
            router = router,
            dialogToTestOverlay = dialogToTestOverlay
        )
}
