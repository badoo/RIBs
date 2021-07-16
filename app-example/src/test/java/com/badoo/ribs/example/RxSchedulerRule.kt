package com.badoo.ribs.example

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulerRule : TestRule {

    private val extension = RxSchedulerExtension()

    override fun apply(
        base: Statement,
        description: Description
    ): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                extension.beforeEach(null)
                base.evaluate()
                extension.afterEach(null)
            }
        }
    }
}
