package com.badoo.ribs.core.helper

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
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
