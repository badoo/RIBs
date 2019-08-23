package com.badoo.ribs.example.rib.small.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.small.Small
import com.badoo.ribs.example.rib.small.SmallNode

class SmallBuilder(
    dependency: Small.Dependency
) : Builder<Small.Dependency>() {

    override val dependency : Small.Dependency = object : Small.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Small::class)
    }

    fun build(savedInstanceState: Bundle?): SmallNode =
        DaggerSmallComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Small.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
