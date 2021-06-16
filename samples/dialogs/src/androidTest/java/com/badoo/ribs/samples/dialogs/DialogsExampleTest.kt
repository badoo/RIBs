package com.badoo.ribs.samples.dialogs

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.test.RibTestActivity
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsExample
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsBuilder
import org.junit.Rule
import org.junit.Test

class DialogsExampleTest {

    @get:Rule
    val ribsRule = RibsRule { activity, savedInstanceState -> buildRib(activity, savedInstanceState) }

    private fun buildRib(ribTestActivity: RibTestActivity, savedInstanceState: Bundle?) =
        DialogsBuilder(object : DialogsExample.Dependency {
            override val dialogLauncher: DialogLauncher =
                ribTestActivity.integrationPoint.dialogLauncher
        }).build(root(savedInstanceState))

    @Test
    fun whenSimpleDialogIsShownThenVerifyTitleIsCorrect() {
        onView(withId(R.id.dialogs_rib_simple_dialog_button)).perform(click())

        onView(withText(R.string.simple_dialog_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun whenThemedDialogIsShownThenVerifyResourcesAreCorrect() {
        onView(withId(R.id.dialogs_rib_themed_dialog_button)).perform(click())

        onView(withText(R.string.themed_dialog_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withId(android.R.id.button1))
            .inRoot(isDialog())
            .check(matches(withTextColor(R.color.blue)))

        onView(withId(android.R.id.button2))
            .inRoot(isDialog())
            .check(matches(withTextColor(R.color.pink)))
    }

    @Test
    fun whenRibDialogIsShownThenVerifyResourcesAreCorrect() {
        onView(withId(R.id.dialogs_rib_rib_dialog_button)).perform(click())

        onView(withText(R.string.dummy_rib_text))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withId(R.id.dummy_rib_button))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun whenLazyDialogIsShownThenVerifyTitleIsCorrect() {
        onView(withId(R.id.dialogs_rib_lazy_dialog_button)).perform(click())

        onView(withText(R.string.lazy_dialog_title))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }
}
