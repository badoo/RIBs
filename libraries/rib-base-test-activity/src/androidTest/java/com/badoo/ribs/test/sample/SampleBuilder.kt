package com.badoo.ribs.test.sample

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class SampleBuilder : SimpleBuilder<SampleRib>() {
    override fun build(buildParams: BuildParams<Nothing?>): SampleRib =
        SampleNode(buildParams)
}
