package com.badoo.ribs.example.matcher

import android.support.annotation.ColorRes
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.Is.`is` as Is

class TextColorMatcher constructor(private val integerMatcher: Matcher<Int>) : BoundedMatcher<View, TextView>(TextView::class.java) {

    public override fun matchesSafely(textView: TextView): Boolean = integerMatcher.matches(textView.currentTextColor)

    override fun describeMismatch(item: Any, mismatchDescription: Description) {
        mismatchDescription.appendText("TextView with text color: " +
            (item as TextView).currentTextColor + ", expected: ")
        integerMatcher.describeMismatch(item, mismatchDescription)
    }

    override fun describeTo(description: Description) {
        description.appendText("with text color ")
        integerMatcher.describeTo(description)
    }
}

fun withTextColor(@ColorRes textColor: Int): Matcher<View> {
    val color = InstrumentationRegistry.getTargetContext().getColor(textColor)
    return TextColorMatcher(Is(color))
}
