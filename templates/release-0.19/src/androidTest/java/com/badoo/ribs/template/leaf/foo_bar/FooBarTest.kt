package com.badoo.ribs.template.leaf.foo_bar

import android.os.Bundle
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.core.builder.BuildContext.Companion.root
import io.reactivex.Observable.empty
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class FooBarTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        FooBarBuilder(object : FooBar.Dependency {
            override fun fooBarInput(): ObservableSource<FooBar.Input> = empty()
            override fun fooBarOutput(): Consumer<FooBar.Output> = Consumer {}
        }).build(root(savedInstanceState))

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
