package com.badoo.ribs.example.rib.dialog_lorem_ipsum

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.RibTestActivity
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.example.R
import com.badoo.ribs.example.app.AppRibCustomisations
import io.reactivex.functions.Consumer
import org.junit.Rule
import org.junit.Test

class DialogLoremIpsumTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        DialogLoremIpsumBuilder(object : DialogLoremIpsum.Dependency {
            override fun ribCustomisation(): RibCustomisationDirectory = AppRibCustomisations
            override fun loremIpsumOutput(): Consumer<DialogLoremIpsum.Output> = Consumer {}
        }).build(savedInstanceState)

    @Test
    fun testTextDisplayed() {
         onView(withId(R.id.lorem_ipsum_text)).check(matches(isDisplayed()))
    }
}
