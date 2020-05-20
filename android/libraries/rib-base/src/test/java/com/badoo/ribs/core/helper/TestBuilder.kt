package com.badoo.ribs.core.helper

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.core.plugin.Plugin

class TestBuilder(
    private val plugins: List<Plugin> = emptyList()
) : SimpleBuilder<TestRib>() {

    override fun build(buildParams: BuildParams<Nothing?>): TestRib =
        TestNode(
            buildParams = buildParams,
            plugins = plugins
        )
}
