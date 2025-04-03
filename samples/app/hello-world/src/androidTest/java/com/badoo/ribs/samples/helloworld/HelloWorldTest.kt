package com.badoo.ribs.samples.helloworld

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.helloworld.hello_world.HelloWorld
import com.badoo.ribs.samples.helloworld.hello_world.builder.HelloWorldBuilder
import com.badoo.ribs.test.RibsRule
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test

private const val HELLO_WORLD_NAME = "world"

class HelloWorldTest {

    @get:Rule
    val ribsRule = RibsRule { _, savedInstanceState -> buildRib(savedInstanceState) }

    private fun buildRib(savedInstanceState: Bundle?) =
        HelloWorldBuilder(object : HelloWorld.Dependency {
            override val name: String = HELLO_WORLD_NAME
        }).build(root(savedInstanceState))

    @Test
    fun showsHelloWorldText() {
        onView(withId(R.id.hello_world_text))
            .check(matches(withText("Hello $HELLO_WORLD_NAME!")))
    }

    @Test
    fun showCountWhenClickingCountButton() {
        onView(withId(R.id.hello_world_button)).perform(click())

        onView(
            allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText("Current count: 1")
            )
        )
            .check(matches(isDisplayed()))
    }
}
