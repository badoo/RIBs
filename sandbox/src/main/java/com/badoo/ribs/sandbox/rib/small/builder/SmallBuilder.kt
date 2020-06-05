package com.badoo.ribs.sandbox.rib.small.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.small.Small

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
