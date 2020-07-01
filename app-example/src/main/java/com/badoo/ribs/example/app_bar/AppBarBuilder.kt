package com.badoo.ribs.example.app_bar

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.app_bar.AppBarBuilder.Params
import com.badoo.ribs.example.image.ImageDownloader
import com.badoo.ribs.example.repository.UserRepository
import io.reactivex.functions.Consumer

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
                output = dependency.appBarOutput,
                userRepository = dependency.userRepository
            ),
            imageDownloader = dependency.imageDownloader
        )

    private fun interactor(
            buildParams: BuildParams<Params>,
            output: Consumer<AppBar.Output>,
            userRepository: UserRepository
    ): AppBarInteractor =
            AppBarInteractor(
                    buildParams = buildParams,
                    output = output,
                    userRepository = userRepository
            )

    private fun node(
            buildParams: BuildParams<Params>,
            customisation: AppBar.Customisation,
            interactor: AppBarInteractor,
            imageDownloader: ImageDownloader
    ) : AppBarNode = AppBarNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(imageDownloader),
            plugins = listOf(interactor)
    )
}
