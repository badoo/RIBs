package com.badoo.ribs.template.rib_with_view.foo_bar

import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.template.rib_with_view.foo_bar.builder.FooBarBuilder
import io.reactivex.Observable.empty
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class FooBarTest {

    @get:Rule
    val ribsRule = RibsRule { buildRib(it) }

    private fun buildRib(ribTestActivity: RibTestActivity) =
        FooBarBuilder(object : FooBar.Dependency {
            override fun fooBarInput(): ObservableSource<FooBar.Input> = empty()
            override fun fooBarOutput(): Consumer<FooBar.Output> = Consumer {}
            override fun ribCustomisation(): RibCustomisationDirectory = TODO()
        }).build()

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
