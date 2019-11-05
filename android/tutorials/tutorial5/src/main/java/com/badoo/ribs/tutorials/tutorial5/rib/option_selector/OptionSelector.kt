package com.badoo.ribs.tutorials.tutorial5.rib.option_selector

import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.RibCustomisation
import com.badoo.ribs.tutorials.tutorial5.util.Lexem
import io.reactivex.functions.Consumer

interface OptionSelector : Rib {

    interface Dependency {
        fun moreOptionsOutput(): Consumer<Output>
        fun moreOptionsConfig(): Config
    }

    data class Config(
        val options: List<Lexem>
    )

    sealed class Output

    class Customisation(
        val viewFactory: OptionSelectorView.Factory = OptionSelectorViewImpl.Factory()
    ) : RibCustomisation
}
