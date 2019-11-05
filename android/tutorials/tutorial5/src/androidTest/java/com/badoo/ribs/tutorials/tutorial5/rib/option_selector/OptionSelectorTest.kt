package com.badoo.ribs.tutorials.tutorial5.rib.more_options

import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.tutorials.tutorial5.rib.more_options.builder.OptionSelectorBuilder
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelector
import io.reactivex.Observable.empty
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class OptionSelectorTest {

    @get:Rule
    val ribsRule = RibsRule { buildRib(it) }

    private fun buildRib(ribTestActivity: RibTestActivity) =
        OptionSelectorBuilder(object : OptionSelector.Dependency {
            override fun moreOptionsInput(): ObservableSource<OptionSelector.Input> = empty()
            override fun moreOptionsOutput(): Consumer<OptionSelector.Output> = Consumer {}
            override fun ribCustomisation(): RibCustomisationDirectory = TODO()
        }).build()

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
