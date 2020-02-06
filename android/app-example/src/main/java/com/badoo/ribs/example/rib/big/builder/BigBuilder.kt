package com.badoo.ribs.example.rib.big.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.big.Big
import com.badoo.ribs.example.rib.big.BigNode

class BigBuilder(
    dependency: Big.Dependency
) : Builder<Big.Dependency, Nothing?, BigNode>() {

    override val dependency : Big.Dependency = object : Big.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Big::class)
    }

    override val rib: Rib =
        object : Big {}

    override fun build(buildParams: BuildParams<Nothing?>): BigNode =
        DaggerBigComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Big.Customisation()),
                buildParams = buildParams
            )
            .node()
}
