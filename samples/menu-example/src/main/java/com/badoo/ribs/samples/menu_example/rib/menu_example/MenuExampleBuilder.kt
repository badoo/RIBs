@file:SuppressWarnings("LongParameterList")

package com.badoo.ribs.samples.menu_example.rib.menu_example

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.menu_example.rib.menu_example.routing.MenuExampleChildBuilders
import com.badoo.ribs.samples.menu_example.rib.menu_example.routing.MenuExampleRouter
import com.badoo.ribs.samples.menu_example.rib.menu_example.routing.MenuExampleRouter.Configuration
import com.badoo.ribs.samples.menu_example.rib.menu_example.routing.MenuExampleRouter.Configuration.Content

class MenuExampleBuilder(
    private val dependency: MenuExample.Dependency
) : SimpleBuilder<MenuExample>() {

    private val builders by lazy { MenuExampleChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): MenuExample {
        val backStack = backStack(buildParams)
        val router = router(buildParams, backStack, builders)
        val presenter = MenuExamplePresenterImpl(backStack)

        return MenuExampleNode(
            buildParams = buildParams,
            viewFactory = MenuExampleViewImpl.Factory().invoke(deps = null),
            plugins = listOfNotNull(router, presenter)
        )
    }

    private fun backStack(buildParams: BuildParams<Nothing?>): BackStack<Configuration> =
        BackStack(
            buildParams = buildParams,
            initialConfiguration = Content.Child1
        )

    private fun router(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStack<Configuration>,
        builders: MenuExampleChildBuilders
    ) = MenuExampleRouter(
        buildParams = buildParams,
        builders = builders,
        routingSource = backStack
    )
}
