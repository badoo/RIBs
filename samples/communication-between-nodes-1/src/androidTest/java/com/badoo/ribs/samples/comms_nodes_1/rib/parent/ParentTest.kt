package com.badoo.ribs.samples.comms_nodes_1.rib.parent

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.comms_nodes_1.R
import org.junit.Rule
import org.junit.Test

class ParentTest {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        ParentBuilder(object : Parent.Dependency {
        }).build(root(savedInstanceState))

    // region Content tests

    @Test
    fun showsA() {
        isViewDisplayed(R.string.i_am_child_a)

        isBackStackMatched("A")
    }

    @Test
    fun pushContent() {
        pushContent(R.id.b_radio_button)

        isViewDisplayed(R.string.i_am_child_b)

        isBackStackMatched("A, B")
    }

    @Test
    fun replaceContent() {
        pushContent(R.id.b_radio_button)

        replaceContent(R.id.c_radio_button)

        isViewDisplayed(R.string.i_am_child_c)

        isBackStackMatched("A, C")
    }

    @Test
    fun popContent() {
        pushContent(R.id.b_radio_button)

        popContentStack()

        isViewDisplayed(R.string.i_am_child_a)

        isBackStackMatched("A")
    }

    @Test
    fun newRootContent() {
        pushContent(R.id.b_radio_button)

        pushContent(R.id.c_radio_button)

        pushContent(R.id.d_radio_button)

        newRootContent(R.id.c_radio_button)

        isViewDisplayed(R.string.i_am_child_c)

        isBackStackMatched("C")
    }

    @Test
    fun singleTopContent() {
        pushContent(R.id.b_radio_button)

        pushContent(R.id.c_radio_button)

        pushContent(R.id.d_radio_button)

        singleTopContent(R.id.b_radio_button)

        isViewDisplayed(R.string.i_am_child_b)

        isBackStackMatched("A, B")
    }

    // endregion

    // region Overlay tests

    @Test
    fun pushOverlay() {
        pushContent(R.id.b_radio_button)

        pushOverlay(R.id.e_radio_button)

        pushOverlay(R.id.f_radio_button)

        isViewDisplayed(R.string.i_am_child_b)

        isViewDisplayed(R.string.i_am_child_e)

        isViewDisplayed(R.string.i_am_child_f)

        isBackStackMatched("A, B + {E, F}")
    }

    @Test
    fun popOverlay() {
        pushContent(R.id.b_radio_button)

        pushOverlay(R.id.e_radio_button)

        pushOverlay(R.id.f_radio_button)

        popOverlayStack()

        isViewDisplayed(R.string.i_am_child_b)

        isViewDisplayed(R.string.i_am_child_e)

        isBackStackMatched("A, B + {E}")
    }

    @Test
    fun popOverlayWithoutOverlays() {
        pushContent(R.id.b_radio_button)

        pushOverlay(R.id.e_radio_button)

        pushContent(R.id.c_radio_button)

        popOverlayStack()

        isViewDisplayed(R.string.i_am_child_c)

        isBackStackMatched("A, B + {E}, C")
    }

    @Test
    fun popContentWithOverlay() {
        pushContent(R.id.b_radio_button)

        pushOverlay(R.id.e_radio_button)

        popContentStack()

        isViewDisplayed(R.string.i_am_child_b)

        isBackStackMatched("A, B")
    }

    // endregion

    // region Helper functions

    private fun pushContent(@IdRes radioButtonId: Int) {
        onView(withId(radioButtonId))
            .perform(click())

        onView(withId(R.id.push_content_button))
            .perform(click())
    }

    private fun replaceContent(@IdRes radioButtonId: Int) {
        onView(withId(radioButtonId))
            .perform(click())

        onView(withId(R.id.replace_content_button))
            .perform(click())
    }

    private fun popContentStack() {
        onView(withId(R.id.pop_content_button))
            .perform(click())
    }

    private fun newRootContent(@IdRes radioButtonId: Int) {
        onView(withId(radioButtonId))
            .perform(click())

        onView(withId(R.id.new_root_content_button))
            .perform(click())
    }

    private fun singleTopContent(@IdRes radioButtonId: Int) {
        onView(withId(radioButtonId))
            .perform(click())

        onView(withId(R.id.single_top_content_button))
            .perform(click())
    }

    private fun isViewDisplayed(@IdRes viewId: Int) {
        onView(withText(viewId))
            .check(matches(isDisplayed()))
    }

    private fun isBackStackMatched(matches: String) {
        onView(withId(R.id.back_stack_text))
            .check(matches(withText("Back stack = [$matches]")))
    }

    private fun pushOverlay(@IdRes radioButtonId: Int) {
        onView(withId(radioButtonId))
            .perform(click())

        onView(withId(R.id.push_overlay_button))
            .perform(click())
    }

    private fun popOverlayStack() {
        onView(withId(R.id.pop_overlay_button))
            .perform(click())
    }

    // endregion

}
