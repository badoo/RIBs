package com.badoo.ribs.sandbox.rib.menu.element

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.matcher.withTextColor

class MenuItemElement(@IdRes id: Int) {
    private val matcher = withId(id)

    fun click() {
        onView(matcher).perform(ViewActions.click())
    }

    fun assertIsSelected() {
        onView(matcher).check(matches(withTextColor(R.color.material_blue_grey_950)))
    }

    fun assertIsNotSelected() {
        onView(matcher).check(matches(withTextColor(R.color.material_grey_600)))
    }
}
