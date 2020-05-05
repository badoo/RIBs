package com.badoo.ribs.example.rib.small.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.example.rib.small.Small

class SmallBuilder(
    private val dependency: Small.Dependency
) : SimpleBuilder<Small>() {

    override fun build(buildParams: BuildParams<Nothing?>): Small =
        DaggerSmallComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(Small.Customisation()),
                buildParams = buildParams
            )
            .node()
}
