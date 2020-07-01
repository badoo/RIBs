package com.badoo.ribs.example.element

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.ribs.example.R
import com.badoo.ribs.example.image.ImageTarget
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf

class AppBarElement {

    private val root: Matcher<View> = withId(R.id.rib_app_bar)

    private val avatarButton: Matcher<View> = descendantWithId(R.id.toolbar_action_start)

    private val searchButton: Matcher<View> = descendantWithId(R.id.toolbar_action_end)

    private val titleView: Matcher<View> = descendantWithId(R.id.toolbar_title)

    fun clickAvatar() {
        onView(avatarButton).perform(click())
    }

    fun clickSearch() {
        onView(searchButton).perform(click())
    }

    fun checkTitle(title: String) {
        onView(titleView).check(matches(withText(title)))
    }

    fun checkShowingPlaceholder() {
        onView(avatarButton)
            .check(matches(withContentDescription(ImageTarget.CONTENT_DESCRIPTION_LOADING)))
    }

    fun checkShowingAvatar() {
        onView(avatarButton)
            .check(matches(withContentDescription(ImageTarget.CONTENT_DESCRIPTION_LOAD_SUCCESS)))
    }

    private fun descendantWithId(id: Int) =
        allOf(
            withId(id),
            isDescendantOfA(root)
        )
}
