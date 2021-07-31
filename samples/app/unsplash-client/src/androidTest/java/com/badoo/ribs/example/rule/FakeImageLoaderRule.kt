package com.badoo.ribs.example.rule

import androidx.test.platform.app.InstrumentationRegistry
import coil.Coil
import com.badoo.ribs.example.TestImageLoader
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class FakeImageLoaderRule : TestRule {

    override fun apply(base: Statement, description: Description?): Statement =
        object : Statement() {
            override fun evaluate() {
                val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                val realImageLoader = Coil.imageLoader(targetContext)

                try {
                    Coil.setImageLoader(TestImageLoader())
                    base.evaluate()
                } finally {
                    Coil.setImageLoader(realImageLoader)
                }
            }

        }
}
