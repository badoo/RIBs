package com.badoo.ribs.example.rib.dialog_example

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.dialog_example.builder.DialogExampleBuilder
import io.reactivex.Observable.empty
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class DialogExampleTest {

    @get:Rule
    val ribsRule = RibsRule { buildRib(it) }

    private fun buildRib(ribTestActivity: RibTestActivity) =
        DialogExampleBuilder(object : DialogExample.Dependency {
            override fun dialogExampleInput(): ObservableSource<DialogExample.Input> = empty()
            override fun dialogExampleOutput(): Consumer<DialogExample.Output> = Consumer {}
            override fun ribCustomisation(): Directory = TODO()
            override fun activityStarter(): ActivityStarter = ribTestActivity
        }).build()

    @Test
    fun testTextDisplayed() {
        TODO("Write UI tests")
        // onView(withId(R.id.some_id)).check(matches(isDisplayed()))
    }
}
