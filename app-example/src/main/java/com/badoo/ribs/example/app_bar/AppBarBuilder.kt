package com.badoo.ribs.example.app_bar

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.app_bar.AppBarBuilder.Params
import com.badoo.ribs.example.repository.UserRepository

class AppBarBuilder(
    private val dependency: AppBar.Dependency
) : Builder<Params, AppBar>() {

    data class Params(
        val userId: String
    )

    override fun build(buildParams: BuildParams<Params>): AppBar =
        node(
            buildParams = buildParams,
            customisation = buildParams.getOrDefault(AppBar.Customisation()),
            interactor = interactor(
                buildParams = buildParams,
                userRepository = dependency.userRepository
            )
        )

    private fun interactor(
        buildParams: BuildParams<Params>,
        userRepository: UserRepository
    ): AppBarInteractor =
        AppBarInteractor(
            buildParams = buildParams,
            userRepository = userRepository
        )

    private fun node(
        buildParams: BuildParams<Params>,
        customisation: AppBar.Customisation,
        interactor: AppBarInteractor
    ): AppBarNode =
        AppBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(interactor)
        )
}
