package com.badoo.ribs.example.rib.menu.element

import android.support.annotation.IdRes
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.badoo.ribs.example.matcher.withTextColor
import com.badoo.ribs.example.R

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