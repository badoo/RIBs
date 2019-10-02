package com.badoo.ribs.example.rib.big.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Builder
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

    override fun build(params: BuildContext.ParamsWithData<Nothing?>): BigNode =
        DaggerBigComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Big.Customisation()),
                buildContext = resolve(object : Big {}, params)
            )
            .node()
}
