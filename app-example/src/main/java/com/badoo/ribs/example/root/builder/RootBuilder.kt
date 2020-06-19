package com.badoo.ribs.example.root.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.root.Root

class RootBuilder(
    private val dependency: Root.Dependency
) : SimpleBuilder<Root>() {

    override fun build(buildParams: BuildParams<Nothing?>): Root =
        DaggerRootComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(Root.Customisation()),
                buildParams = buildParams
            )
            .node()
}
