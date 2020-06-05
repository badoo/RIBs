package com.badoo.ribs.tutorials.tutorial5.rib.option_selector.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelector
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelector.Output
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelectorInteractor
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelectorView
import dagger.Provides
import io.reactivex.functions.Consumer

@dagger.Module
internal object OptionSelectorModule {

    @OptionSelectorScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        output: Consumer<Output>,
        config: OptionSelector.Config
    ): OptionSelectorInteractor =
        OptionSelectorInteractor(
            buildParams = buildParams,
            output = output,
            options = config.options
        )

    @OptionSelectorScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: OptionSelector.Customisation,
        interactor: OptionSelectorInteractor
    ) : Node<OptionSelectorView> = Node(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory.invoke(null),
        plugins = listOf(interactor)
    )
}
