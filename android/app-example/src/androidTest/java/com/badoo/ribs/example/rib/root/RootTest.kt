package com.badoo.ribs.example.rib.root

import android.os.Bundle
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.example.rib.root.builder.RootBuilder
import io.reactivex.Observable.empty
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class RootTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        RootBuilder(object : Root.Dependency {
            override fun rootInput(): ObservableSource<Root.Input> = empty()
            override fun rootOutput(): Consumer<Root.Output> = Consumer {}
            override fun ribCustomisation(): RibCustomisationDirectory = TODO()
        }).build(savedInstanceState)

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
    }
}
