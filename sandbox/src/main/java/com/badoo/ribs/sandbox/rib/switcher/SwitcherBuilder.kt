package com.badoo.ribs.sandbox.rib.switcher

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.rx.adapter.rx2
import com.badoo.ribs.sandbox.BuildConfig
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

    private val builders by lazy { SwitcherChildBuilders(dependency) }
    private val dialogToTestOverlay = DialogToTestOverlay()
    private val viewDependency: SwitcherView.Dependency =
        object : SwitcherView.Dependency {
            override fun coffeeMachine(): CoffeeMachine = StupidCoffeeMachine()
        }

    override fun build(buildParams: BuildParams<Nothing?>): SwitcherNode {
        val customisation = buildParams.getOrDefault(Switcher.Customisation())
        val backStack  = backStack(buildParams)
        val router = router(buildParams, customisation, backStack)
        val interactor = interactor(buildParams, backStack, router)

        return SwitcherNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(viewDependency),
            backStack = backStack,
            plugins = listOfNotNull(
                router,
                interactor,
                SwitcherDebugControls().takeIf { BuildConfig.DEBUG }
            )
        )
    }

    private fun backStack(buildParams: BuildParams<Nothing?>): BackStack<Configuration> =
        BackStack(
            buildParams = buildParams,
            initialConfiguration = Content.Foo
        )

    private fun interactor(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStack<Configuration>,
        router: Router<*>
    ): SwitcherInteractor =
        SwitcherInteractor(
            buildParams = buildParams,
            backStack = backStack,
            transitions = router.transitionStates.rx2().distinctUntilChanged(),
            transitionSettled = { router.transitionState.isSettled },
            dialogToTestOverlay = dialogToTestOverlay
        )

    private fun router(
        buildParams: BuildParams<Nothing?>,
        customisation: Switcher.Customisation,
        backStack: BackStack<Configuration>
    ): SwitcherRouter =
        SwitcherRouter(
            buildParams = buildParams,
            routingSource = backStack,
            transitionHandler = customisation.transitionHandler,
            builders = builders,
            dialogLauncher = dependency.dialogLauncher,
            dialogToTestOverlay = dialogToTestOverlay
        )
}
