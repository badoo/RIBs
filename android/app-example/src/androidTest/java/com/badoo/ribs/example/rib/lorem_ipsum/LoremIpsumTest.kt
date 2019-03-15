package com.badoo.ribs.example.rib.lorem_ipsum

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.lorem_ipsum.builder.LoremIpsumBuilder
import io.reactivex.Observable.empty
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class LoremIpsumTest {

    @get:Rule
    val ribsRule = RibsRule { buildRib(it) }

    private fun buildRib(ribTestActivity: RibTestActivity) =
        LoremIpsumBuilder(object : LoremIpsum.Dependency {
            override fun loremIpsumInput(): ObservableSource<LoremIpsum.Input> = empty()
            override fun loremIpsumOutput(): Consumer<LoremIpsum.Output> = Consumer {}
            override fun ribCustomisation(): Directory = TODO()
            override fun activityStarter(): ActivityStarter = ribTestActivity
        }).build()

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
        // onView(withId(R.id.some_id)).check(matches(isDisplayed()))
    }
}
