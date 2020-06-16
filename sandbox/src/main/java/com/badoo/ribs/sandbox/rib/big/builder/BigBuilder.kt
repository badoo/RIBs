package com.badoo.ribs.sandbox.rib.big.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.big.Big

class BigBuilder() : SimpleBuilder<Big>() {

    override fun build(buildParams: BuildParams<Nothing?>): Big = TODO()
//        DaggerBigComponent
//            .factory()
//            .create(
//                dependency = dependency,
//                customisation = buildParams.getOrDefault(Big.Customisation()),
//                buildParams = buildParams
//            )
//            .node()
}
